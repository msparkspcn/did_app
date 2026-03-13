package com.secta9ine.didapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.data.repository.DidV2Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DidV2ViewModel @Inject constructor(
    private val repository: DidV2Repository
) : ViewModel() {
    data class PlayerUiState(
        val stage: Stage = Stage.CHECKING_DEVICE,
        val message: String? = null
    )

    enum class Stage {
        CHECKING_DEVICE,
        PENDING_APPROVAL,
        BLOCKED,
        READY,
        ERROR
    }

    private val didId = "did-001"
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState

    val snapshot: StateFlow<PlayerSnapshotDto?> = repository.snapshotFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val isSleeping: StateFlow<Boolean> = repository.isSleeping

    init {
        viewModelScope.launch {
            when (val access = repository.resolveDeviceAccess(didId)) {
                DidV2Repository.DeviceAccess.Active -> {
                    _uiState.value = PlayerUiState(stage = Stage.READY)
                    repository.syncPowerSchedule(didId)
                    repository.startSleepCheckLoop(viewModelScope)
                    repository.syncInitialSnapshot(didId)
                    repository.startRealtime(didId, viewModelScope)
                }
                DidV2Repository.DeviceAccess.PendingApproval -> {
                    _uiState.value = PlayerUiState(
                        stage = Stage.PENDING_APPROVAL,
                        message = "Device is registered.\nWaiting for admin approval."
                    )
                }
                is DidV2Repository.DeviceAccess.Blocked -> {
                    _uiState.value = PlayerUiState(
                        stage = Stage.BLOCKED,
                        message = "Device access blocked: ${access.status}"
                    )
                }
                is DidV2Repository.DeviceAccess.Error -> {
                    _uiState.value = PlayerUiState(
                        stage = Stage.ERROR,
                        message = "Device auth failed: ${access.reason}"
                    )
                }
            }
        }
    }

    fun onPowerStateChanged(sleeping: Boolean) {
        repository.updateSleepState(sleeping)
    }

    override fun onCleared() {
        repository.stopRealtime()
        super.onCleared()
    }
}
