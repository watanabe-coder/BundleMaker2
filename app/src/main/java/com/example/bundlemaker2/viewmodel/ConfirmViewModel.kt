package com.example.bundlemaker2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.usecase.ConfirmMappingsUseCase
import com.example.bundlemaker2.data.entity.MfgSerialMapping
import com.example.bundlemaker2.usecase.GetMappingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfirmScreenState(
    val mappings: List<MfgSerialMapping> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isConfirmed: Boolean = false
)

class ConfirmViewModel @Inject constructor(
    private val confirmMappingsUseCase: ConfirmMappingsUseCase,
    private val getMappingsUseCase: GetMappingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConfirmScreenState())
    val state: StateFlow<ConfirmScreenState> = _state

    fun loadMappings(mfgId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val mappings = getMappingsUseCase.getMappingsByMfgId(mfgId)
            _state.value = _state.value.copy(
                mappings = mappings,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    fun deleteMapping(mappingId: Long) {
        viewModelScope.launch {
            // 削除処理はUseCase経由で実装
            val result = confirmMappingsUseCase.deleteMapping(mappingId)
            if (result.isSuccess) {
                // 再読込
                loadMappings(_state.value.mappings.firstOrNull()?.mfgId ?: "")
            } else {
                _state.value = _state.value.copy(
                    errorMessage = result.exceptionOrNull()?.message ?: "削除失敗"
                )
            }
        }
    }

    fun confirmMappings(mfgId: String) {
        viewModelScope.launch {
            val result = confirmMappingsUseCase.confirm(mfgId)
            _state.value = if (result.isSuccess) {
                _state.value.copy(isConfirmed = true, errorMessage = null)
            } else {
                _state.value.copy(errorMessage = result.exceptionOrNull()?.message ?: "確定失敗")
            }
        }
    }
}