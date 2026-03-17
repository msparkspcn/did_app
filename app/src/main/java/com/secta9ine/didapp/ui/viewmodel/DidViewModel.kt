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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DidViewModel @Inject constructor(
    application: Application,
    private val repository: DidRepository,
    private val logger: FileLogger
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

        repository.subscribeDeviceEvents(deviceId) { event ->
            viewModelScope.launch {
                handleDeviceEvent(event)
            }
        }

        // 2. GET /api/v1/device-auth/{deviceId} 인증 상태 조회
        viewModelScope.launch {
            when (val access = repository.resolveDeviceAccess(deviceId)) {
                is DidRepository.DeviceAccess.Active -> {
                    logger.i(TAG, "Stage → AUTHENTICATED deviceId=${access.deviceId}")
                    _uiState.value = UiState(
                        stage = Stage.AUTHENTICATED,
                        deviceId = access.deviceId
                    )
                }
                is DidRepository.DeviceAccess.PendingApproval -> {
                    logger.i(TAG, "Stage → PENDING_APPROVAL approvalCode=${access.approvalCode}")
                    _uiState.value = UiState(
                        stage = Stage.PENDING_APPROVAL,
                        approvalCode = access.approvalCode
                    )
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
                logger.i(TAG, "Stage → AUTHENTICATED (via WS) deviceId=${event.data.deviceId} authStatus=${event.data.authStatus}")
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

    fun retry() {
        logger.i(TAG, "retry()")
        startDeviceAuth()
    }

    override fun onCleared() {
        super.onCleared()
        logger.i(TAG, "onCleared() - unsubscribing")
        repository.unsubscribeDeviceEvents()
    }
}
