package com.example.bundlemaker2.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MfgSerialMappingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    // 選択中のマッピングID
    private val _selectedIds = mutableSetOf<Long>()
    val selectedIds: Set<Long> get() = _selectedIds

    // 選択状態の切り替え
    fun toggleSelection(id: Long) {
        if (_selectedIds.contains(id)) {
            _selectedIds.remove(id)
        } else {
            _selectedIds.add(id)
        }
        _uiState.value = _uiState.value.copy(selectedCount = _selectedIds.size)
    }

    // 選択をクリア
    fun clearSelection() {
        _selectedIds.clear()
        _uiState.value = _uiState.value.copy(selectedCount = 0)
    }

    // ステータスを一括更新
    fun updateStatus(status: MappingStatus) {
        if (_selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                repository.updateStatuses(_selectedIds.toList(), status).onSuccess {
                    clearSelection()
                    _uiState.value = _uiState.value.copy(
                        message = "${_selectedIds.size}件を更新しました",
                        showMessage = true
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "エラーが発生しました",
                        showError = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "エラーが発生しました",
                    showError = true
                )
            }
        }
    }

    // 選択したアイテムを削除
    fun deleteSelected() {
        if (_selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                // Delete each selected mapping one by one
                _selectedIds.forEach { id ->
                    repository.getById(id)?.let { mapping ->
                        repository.delete(mapping)
                    }
                }
                clearSelection()
                _uiState.value = _uiState.value.copy(
                    message = "${_selectedIds.size}件を削除しました",
                    showMessage = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "削除に失敗しました",
                    showError = true
                )
            }
        }
    }

    // メッセージを閉じる
    fun dismissMessage() {
        _uiState.value = _uiState.value.copy(showMessage = false, showError = false)
    }
}

// UI状態を保持するデータクラス
data class MainUiState(
    val selectedCount: Int = 0,
    val message: String = "",
    val error: String = "",
    val showMessage: Boolean = false,
    val showError: Boolean = false
)