package com.secta9ine.didapp.ui.viewmodel

import android.app.Application
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.didapp.data.remote.DeviceEvent
import com.secta9ine.didapp.data.remote.QueueCalledData
import com.secta9ine.didapp.data.remote.ScreenUpdatedData
import com.secta9ine.didapp.data.remote.ProductUpdatedData
import com.secta9ine.didapp.data.remote.UpdateData
import com.secta9ine.didapp.data.repository.DidRepository
import com.secta9ine.didapp.system.FileLogger
import com.secta9ine.didapp.system.NetworkMonitor
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.secta9ine.didapp.system.QuberAgentManager
import com.secta9ine.didapp.system.QuberResponse
import kotlinx.coroutines.delay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DidViewModel @Inject constructor(
    application: Application,
    private val repository: DidRepository,
    private val logger: FileLogger,
    private val networkMonitor: NetworkMonitor,
    private val quberAgentManager: QuberAgentManager
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "DidViewModel"
    }

    data class UiState(
        val stage: Stage = Stage.CHECKING_DEVICE,
        val message: String? = null,
        val approvalCode: String? = null,
        val deviceId: String? = null
    )

    enum class Stage {
        CHECKING_DEVICE,
        PENDING_APPROVAL,
        AUTHENTICATED,
        OFFLINE_ACTIVE,      // 이전 인증 이력 있음 + 서버 통신 실패
        SERVER_UNREACHABLE,  // 인증 이력 없음 + 서버 통신 실패
        ERROR
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _screenUpdated = MutableSharedFlow<ScreenUpdatedData>(extraBufferCapacity = 1)
    val screenUpdated: SharedFlow<ScreenUpdatedData> = _screenUpdated

    private val _productUpdated = MutableSharedFlow<ProductUpdatedData>(extraBufferCapacity = 1)
    val productUpdated: SharedFlow<ProductUpdatedData> = _productUpdated

    private val _queueCalled = MutableSharedFlow<QueueCalledData>(extraBufferCapacity = 1)
    val queueCalled: SharedFlow<QueueCalledData> = _queueCalled

    private val _restartRequested = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val restartRequested: SharedFlow<Unit> = _restartRequested

    private val _updateRequested = MutableSharedFlow<UpdateData>(extraBufferCapacity = 1)
    val updateRequested: SharedFlow<UpdateData> = _updateRequested

    private val _powerOff = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val powerOff: SharedFlow<Unit> = _powerOff

    private val _powerOn = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val powerOn: SharedFlow<Unit> = _powerOn

    private var networkRetryJob: Job? = null

    private val deviceId: String by lazy {
        Settings.Secure.getString(
            getApplication<Application>().contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"
    }

    init {
        startDeviceAuth()
    }

    private fun startDeviceAuth() {
        logger.i(TAG, "startDeviceAuth() deviceId=$deviceId")
        _uiState.value = UiState(stage = Stage.CHECKING_DEVICE)

        // STOMP 연결 → /topic/devices/{deviceId} 구독
        repository.subscribeDeviceEvents(deviceId) { event ->
            viewModelScope.launch {
                handleDeviceEvent(event)
            }
        }

        // 인증 상태 확인 (서버 우선, 실패 시 로컬 fallback)
        viewModelScope.launch {
            when (val access = repository.resolveDeviceAccess(deviceId)) {
                is DidRepository.DeviceAccess.Active -> {
                    logger.i(TAG, "Stage → AUTHENTICATED deviceId=${access.deviceId}")
                    _uiState.value = UiState(
                        stage = Stage.AUTHENTICATED,
                        deviceId = access.deviceId
                    )
                    setupScheduleAndAutoRun()
                }
                is DidRepository.DeviceAccess.PendingApproval -> {
                    logger.i(TAG, "Stage → PENDING_APPROVAL approvalCode=${access.approvalCode}")
                    _uiState.value = UiState(
                        stage = Stage.PENDING_APPROVAL,
                        approvalCode = access.approvalCode
                    )
                }
                is DidRepository.DeviceAccess.OfflineActive -> {
                    logger.i(TAG, "Stage → OFFLINE_ACTIVE deviceId=${access.deviceId}")
                    _uiState.value = UiState(
                        stage = Stage.OFFLINE_ACTIVE,
                        deviceId = access.deviceId,
                        message = "오프라인 모드로 동작 중"
                    )
                    waitForNetworkAndRetry()
                }
                is DidRepository.DeviceAccess.ServerUnreachable -> {
                    logger.w(TAG, "Stage → SERVER_UNREACHABLE")
                    _uiState.value = UiState(
                        stage = Stage.SERVER_UNREACHABLE,
                        message = access.reason
                    )
                    waitForNetworkAndRetry()
                }
                is DidRepository.DeviceAccess.Error -> {
                    logger.e(TAG, "Stage → ERROR reason=${access.reason}")
                    _uiState.value = UiState(
                        stage = Stage.ERROR,
                        message = access.reason
                    )
                }
            }
        }
    }

    private fun handleDeviceEvent(event: DeviceEvent) {
        logger.i(TAG, "handleDeviceEvent() type=${event::class.simpleName} deviceId=${event.deviceId}")
        when (event) {
            is DeviceEvent.Activated -> {
                logger.i(TAG, "Stage → AUTHENTICATED (via WS) deviceId=${event.data.deviceId}")
                viewModelScope.launch {
                    repository.onDeviceActivated(event)
                }
                _uiState.value = UiState(
                    stage = Stage.AUTHENTICATED,
                    deviceId = event.data.deviceId
                )
            }
            is DeviceEvent.ScreenUpdated -> {
                logger.i(TAG, "ScreenUpdated: screenId=${event.data.screenId}")
                _screenUpdated.tryEmit(event.data)
            }
            is DeviceEvent.ProductUpdated -> {
                logger.i(TAG, "ProductUpdated: productId=${event.data.productId}")
                _productUpdated.tryEmit(event.data)
            }
            is DeviceEvent.QueueCalled -> {
                logger.i(TAG, "QueueCalled: number=${event.data.queueNumber} counter=${event.data.counterName}")
                _queueCalled.tryEmit(event.data)
            }
            is DeviceEvent.Restart -> {
                logger.i(TAG, "Restart requested")
                _restartRequested.tryEmit(Unit)
            }
            is DeviceEvent.Update -> {
                logger.i(TAG, "Update requested: version=${event.data.version}")
                _updateRequested.tryEmit(event.data)
            }
            is DeviceEvent.PowerOn -> {
                logger.i(TAG, "PowerOn requested")
                _powerOn.tryEmit(Unit)
            }
            is DeviceEvent.PowerOff -> {
                logger.i(TAG, "PowerOff requested")
                _powerOff.tryEmit(Unit)
            }
        }
    }

    /**
     * 네트워크 복구를 감지하면 자동으로 인증 플로우를 재시작한다.
     */
    private fun waitForNetworkAndRetry() {
        networkRetryJob?.cancel()
        networkRetryJob = viewModelScope.launch {
            logger.d(TAG, "Waiting for network recovery...")
            networkMonitor.isConnected.filter { it }.first()
            logger.i(TAG, "Network recovered, retrying auth")
            startDeviceAuth()
        }
    }

    /**
     * [TEST] 일정 설정 (10:35 종료, 10:40 시작) + AutoRun 설정
     */
    private fun setupScheduleAndAutoRun() {
        viewModelScope.launch {
            // QuberAgent 바인딩 대기
            var waitCount = 0
            while (!quberAgentManager.isConnected && waitCount < 10) {
                delay(500)
                waitCount++
            }
            if (!quberAgentManager.isConnected) {
                logger.e(TAG, "[SCHEDULE] QuberAgent 연결 실패")
                return@launch
            }

            // 1. AutoRun 설정 - 디바이스 시작 시 이 앱 자동 실행
            logger.i(TAG, "[SCHEDULE] AutoRun 설정 시작")
            val autoRunResult = quberAgentManager.setAutoRun(
                "com.secta9ine.didapp",
                "com.secta9ine.didapp.ui.MainActivity"
            )
            logger.i(TAG, "[SCHEDULE] AutoRun 결과: $autoRunResult")

            // 2. 일정 설정 - 수요일(4) 13:37 sleep, 13:39 reboot, 13:41 wake
            logger.i(TAG, "[SCHEDULE] 일정 설정: sleep=13:37, reboot=13:39, wake=13:41")
            val scheduleEntry = JsonObject().apply {
                addProperty("dayOfWeek", 4) // 수요일
                addProperty("rebootTime", "13:39")
                addProperty("sleepTime", "13:37")
                addProperty("wakeupTime", "13:41")
            }
            val params = JsonArray().apply { add(scheduleEntry) }
            val scheduleResult = quberAgentManager.sendCommand(
                QuberAgentManager.CmdCode.SCHEDULE_SET,
                params
            )
            logger.i(TAG, "[SCHEDULE] 일정 설정 결과: $scheduleResult")

            // 3. 설정 확인
            val readResult = quberAgentManager.readSchedule()
            logger.i(TAG, "[SCHEDULE] 현재 일정 조회: $readResult")

            val autoRunRead = quberAgentManager.readAutoRun()
            logger.i(TAG, "[SCHEDULE] 현재 AutoRun 조회: $autoRunRead")
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkRetryJob?.cancel()
        logger.i(TAG, "onCleared() - unsubscribing")
        repository.unsubscribeDeviceEvents()
    }
}
