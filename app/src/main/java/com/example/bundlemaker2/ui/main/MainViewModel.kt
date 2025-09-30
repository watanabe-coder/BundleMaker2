package com.example.bundlemaker2.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.repository.MfgSerialRepository
import com.example.bundlemaker2.ui.main.MainUiState.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MfgSerialRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState

    // 選択中のマッピングID
    private val _selectedIds = mutableSetOf<Long>()
    val selectedIds: Set<Long> get() = _selectedIds

    // マッピングをIDで取得
    private suspend fun getMappingById(id: Long): MfgSerialMapping? {
        return repository.getById(id).getOrNull()
    }

    // 選択状態の切り替え
    fun toggleSelection(id: Long) {
        if (_selectedIds.contains(id)) {
            _selectedIds.remove(id)
        } else {
            _selectedIds.add(id)
        }
        updateUiState { it.copy(selectedCount = _selectedIds.size) }
    }

    // 選択をクリア
    fun clearSelection() {
        _selectedIds.clear()
        updateUiState { it.copy(selectedCount = 0) }
    }

    // ステータスを一括更新
    fun updateStatus(newStatus: MappingStatus) {
        if (_selectedIds.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            try {
                _selectedIds.forEach { id ->
                    val result = getMappingById(id)
                    result?.let { mapping ->
                        val updated = mapping.copy(status = newStatus)
                        repository.update(updated)
                    }
                }

                clearSelection()
                showMessage("${_selectedIds.size}件を更新しました")
            } catch (e: Exception) {
                showError("更新に失敗しました: ${e.message}")
            }
        }
    }

    // 選択したアイテムを削除
    fun deleteSelected() {
        if (_selectedIds.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            try {
                _selectedIds.forEach { id ->
                    val mapping = getMappingById(id)
                    mapping?.let { repository.delete(it) }
                }

                clearSelection()
                showMessage("${_selectedIds.size}件を削除しました")
            } catch (e: Exception) {
                showError("削除に失敗しました: ${e.message}")
            }
        }
    }

    // メッセージを表示
    private fun showMessage(message: String) {
        _uiState.value = MainUiState.Success(message)
    }

    // エラーを表示
    private fun showError(message: String) {
        _uiState.value = MainUiState.Error(message)
    }

    // UI状態を更新（ヘルパー関数）
    private fun updateUiState(update: (Data) -> Data) {
        val current = _uiState.value as? Data ?: return
        _uiState.value = update(current)
    }
}

// UIの状態を表すシールドクラス
sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val message: String) : MainUiState()
    data class Error(val message: String) : MainUiState()
    data class Data(
        val selectedCount: Int = 0,
        val message: String = "",
        val showMessage: Boolean = false,
        val error: String = "",
        val showError: Boolean = false
    ) : MainUiState() {
        // Remove the custom copy function as it's causing ambiguity
        // The data class automatically generates a copy function
    }
}