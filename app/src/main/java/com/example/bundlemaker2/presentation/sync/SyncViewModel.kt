package com.example.bundlemaker2.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.domain.usecase.SyncMfgSerialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.bundlemaker2.domain.usecase.Result

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncUseCase: SyncMfgSerialsUseCase
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private var syncJob: Job? = null

    fun syncMfgSerials() {
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            _syncState.value = SyncState.Loading

            val result = syncUseCase { current, total ->
                _syncState.value = SyncState.Progress(current, total)
            }

            when (result) {
                is Result.Success -> {
                    _syncState.value = SyncState.Success(result.data.message)
                }
                is Result.Error -> {
                    _syncState.value = SyncState.Error(result.message)
                }
            }
        }
    }

    fun retrySync() {
        syncMfgSerials()
    }

    fun resetState() {
        _syncState.value = SyncState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        syncJob?.cancel()
    }
}