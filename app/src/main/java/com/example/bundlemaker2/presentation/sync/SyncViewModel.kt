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

sealed class SyncProgress {
    data class Update(val current: Int, val total: Int) : SyncProgress()
    data class Success(
        val processedCount: Int,
        val successCount: Int,
        val failedCount: Int
    ) : SyncProgress()
    data class Error(val message: String) : SyncProgress()
}

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncUseCase: SyncMfgSerialsUseCase
) : ViewModel() {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private var syncJob: Job? = null

    fun syncData() {
        if (syncJob?.isActive == true) return

        syncJob = viewModelScope.launch {
            try {
                _syncState.value = SyncState.Loading(current = 0, total = 0)

                // 進捗コールバックを定義
                val onProgress: (Int, Int) -> Unit = { current, total ->
                    _syncState.value = SyncState.Loading(current, total)
                }

                // 同期を実行
                val result = syncUseCase(onProgress)

                when (result) {
                    is Result.Success -> {
                        _syncState.value = SyncState.Success(
                            message = result.data.message,
                            processedCount = result.data.successCount + result.data.failureCount,
                            successCount = result.data.successCount,
                            failedCount = result.data.failureCount
                        )
                    }
                    is Result.Error -> {
                        _syncState.value = SyncState.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _syncState.value = SyncState.Error("同期に失敗しました: ${e.message}")
            }
        }
    }

    fun retrySync() {
        syncData()
    }

    fun resetState() {
        _syncState.value = SyncState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        syncJob?.cancel()
    }
}