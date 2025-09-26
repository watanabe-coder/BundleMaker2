// app/src/main/java/com/example/bundlemaker2/ui/main/MainViewModel.kt
package com.example.bundlemaker2.ui.main

import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.data.entity.MappingStatus
import com.example.bundlemaker2.data.repository.MfgSerialRepository
import com.example.bundlemaker2.data.repository.WorkSessionRepository
import com.example.bundlemaker2.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class MainUiState(
    val mfgId: String = "",
    val serialId: String = "",
    val scannedCount: Int = 0,
    val successCount: Int = 0,
    val errorCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSessionActive: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mfgSerialRepository: MfgSerialRepository,
    private val workSessionRepository: WorkSessionRepository
) : BaseViewModel<MainUiState>() {

    override fun initialUiState(): MainUiState = MainUiState()

    init {
        loadCounts()
        checkActiveSession()
    }

    private fun loadCounts() {
        viewModelScope.launch {
            mfgSerialRepository.countByStatus(MappingStatus.SUCCESS).collect { successCount ->
                _uiState.value = _uiState.value.copy(successCount = successCount)
            }
        }

        viewModelScope.launch {
            mfgSerialRepository.countByStatus(MappingStatus.ERROR).collect { errorCount ->
                _uiState.value = _uiState.value.copy(errorCount = errorCount)
            }
        }
    }

    fun onMfgIdChanged(mfgId: String) {
        _uiState.value = _uiState.value.copy(mfgId = mfgId)
    }

    fun onSerialIdChanged(serialId: String) {
        _uiState.value = _uiState.value.copy(serialId = serialId)
    }

    fun onScanButtonClicked() {
        val mfgId = _uiState.value.mfgId
        val serialId = _uiState.value.serialId

        if (mfgId.isBlank() || serialId.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "製造番号とシリアル番号を入力してください")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // マッピングを追加
                val result = mfgSerialRepository.addMapping(mfgId, serialId, Instant.now())

                result.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        serialId = "",
                        scannedCount = _uiState.value.scannedCount + 1,
                        error = null
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "エラーが発生しました"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "予期せぬエラーが発生しました"
                )
            }
        }
    }

    private fun checkActiveSession() {
        viewModelScope.launch {
            // アクティブなセッションがあるか確認
            val activeSessions = workSessionRepository.getActiveSessions()
            activeSessions.collectLatest { sessions ->
                _uiState.value = _uiState.value.copy(isSessionActive = sessions.isNotEmpty())
            }
        }
    }

    fun startNewSession() {
        val mfgId = _uiState.value.mfgId
        if (mfgId.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "製造番号を入力してください")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = workSessionRepository.startSession(mfgId)
                result.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSessionActive = true
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "セッションの開始に失敗しました"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "予期せぬエラーが発生しました"
                )
            }
        }
    }

    fun endCurrentSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // 最新のセッションを取得して終了
                val latestSession = workSessionRepository.getLatestSessionByMfgId(_uiState.value.mfgId)
                latestSession?.let { session ->
                    val result = workSessionRepository.endSession(session.id)
                    result.onSuccess {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSessionActive = false,
                            mfgId = "",
                            serialId = ""
                        )
                    }.onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "セッションの終了に失敗しました"
                        )
                    }
                } ?: run {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "アクティブなセッションが見つかりません"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "予期せぬエラーが発生しました"
                )
            }
        }
    }
}