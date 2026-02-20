package com.example.didapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.didapp.data.local.DidEntity
import com.example.didapp.data.repository.DidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DidViewModel @Inject constructor(
    private val repository: DidRepository
) : ViewModel() {

    val didItems: StateFlow<List<DidEntity>> = repository.allDidItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        syncData()
    }

    fun syncData() {
        viewModelScope.launch {
            repository.syncWithRemote()
        }
    }
}
