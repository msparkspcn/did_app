package com.secta9ine.didapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.data.repository.DidV2Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DidV2ViewModel @Inject constructor(
    private val repository: DidV2Repository
) : ViewModel() {
    private val didId = "did-001"

    val snapshot: StateFlow<PlayerSnapshotDto?> = repository.snapshotFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            repository.syncInitialSnapshot(didId)
            repository.startRealtime(didId, viewModelScope)
        }
    }

    override fun onCleared() {
        repository.stopRealtime()
        super.onCleared()
    }
}
