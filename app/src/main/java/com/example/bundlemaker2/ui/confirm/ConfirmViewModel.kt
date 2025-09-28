package com.example.bundlemaker2.ui.confirm

import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository
import com.example.bundlemaker2.domain.repository.WorkSessionRepository
import com.example.bundlemaker2.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfirmUiState(
    val mfgId: String = "",
    val serialCount: Int = 0,
    val successCount: Int = 0,
    val errorCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isConfirmed: Boolean = false
)

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val mfgSerialRepository: MfgSerialMappingRepository,
    private val workSessionRepository: WorkSessionRepository
) : BaseViewModel<ConfirmUiState>() {

    override fun initialUiState(): ConfirmUiState = ConfirmUiState()

    init {
        loadSummary()
    }

    private fun loadSummary() {
        viewModelScope.launch {
            // 製造番号でフィルタリングして集計
            val mfgId = _uiState.value.mfgId
            if (mfgId.isNotBlank()) {
                mfgSerialRepository.getByMfgId(mfgId).collect { mappings ->  // 変更
                    val successCount = mappings.count { it.status == MappingStatus.SENT }  // 変更
                    val errorCount = mappings.count { it.status == MappingStatus.ERROR }

                    _uiState.value = _uiState.value.copy(
                        serialCount = mappings.size,
                        successCount = successCount,
                        errorCount = errorCount
                    )
                }
            }
        }
    }

    fun onMfgIdChanged(mfgId: String) {
        _uiState.value = _uiState.value.copy(mfgId = mfgId)
        loadSummary()
    }

    fun onConfirm() {
        val mfgId = _uiState.value.mfgId
        if (mfgId.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "製造番号が指定されていません")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // 最新のセッションを取得
                val latestSession = workSessionRepository.getLatestByMfgId(mfgId)
                latestSession?.let { session ->
                    // セッションを終了（現在時刻をendTimeとして渡す）
                    workSessionRepository.endSession(session.id, java.time.Instant.now())
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isConfirmed = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "確認処理中にエラーが発生しました"
                )
            }
        }
    }

    fun onCancel() {
        // キャンセル処理
        _uiState.value = _uiState.value.copy(isConfirmed = false)
    }
}