package com.example.bundlemaker2.ui.confirm

import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.data.entity.MappingStatus
import com.example.bundlemaker2.data.repository.MfgSerialRepository
import com.example.bundlemaker2.data.repository.WorkSessionRepository
import com.example.bundlemaker2.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
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
    private val mfgSerialRepository: MfgSerialRepository,
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
                mfgSerialRepository.getMappingsByMfgId(mfgId).collect { mappings ->
                    val successCount = mappings.count { it.status == MappingStatus.SUCCESS }
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
                // ここで確認処理を実装
                // 例: ステータスをSENTに更新
                // セッションを終了
                val latestSession = workSessionRepository.getLatestSessionByMfgId(mfgId)
                latestSession?.let { session ->
                    workSessionRepository.endSession(session.id)
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