package com.example.bundlemaker2.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.data.model.MfgSerialMapping
import com.example.bundlemaker2.data.repository.MfgSerialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MfgSerialRepository
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
    fun updateStatus(newStatus: String) {
        if (_selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                // 選択された各IDのステータスを更新
                _selectedIds.forEach { id ->
                    // リポジトリから現在のマッピングを取得して更新
                    val mapping = repository.getMappingById(id)
                    mapping?.let { mapping ->
                        val updated = mapping.copy(status = newStatus)
                        repository.update(updated)
                    }
                }
                
                clearSelection()
                _uiState.update { currentState ->
                    currentState.copy(
                        message = "${_selectedIds.size}件を更新しました",
                        showMessage = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        error = e.message ?: "エラーが発生しました",
                        showError = true
                    )
                }
            }
        }
    }

    // 選択したアイテムを削除
    fun deleteSelected() {
        if (_selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                // 選択された各IDを削除
                _selectedIds.forEach { id ->
                    val mapping = repository.getMappingById(id)
                    mapping?.let { repository.delete(it) }
                }
                
                clearSelection()
                _uiState.update { currentState ->
                    currentState.copy(
                        message = "${_selectedIds.size}件を削除しました",
                        showMessage = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        error = e.message ?: "削除に失敗しました",
                        showError = true
                    )
                }
            }
        }
    }

    // メッセージを閉じる
    fun dismissMessage() {
        _uiState.value = _uiState.value.copy(showMessage = false, showError = false)
    }
}

// UI状態を保持するデータクラス
/**
 * UI状態を保持するデータクラス
 */
data class MainUiState(
    val selectedCount: Int = 0,
    val message: String = "",
    val showMessage: Boolean = false,
    val error: String = "",
    val showError: Boolean = false,
    val isLoading: Boolean = false
)