package com.example.bundlemaker2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.usecase.AppendMappingUseCase
import com.example.bundlemaker2.data.local.entity.MfgSerialMapping
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainScreenState(
    val mappings: List<MfgSerialMapping> = emptyList(),
    val unsentCount: Int = 0,
    val errorCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MainViewModel @Inject constructor(
    private val appendMappingUseCase: AppendMappingUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state

    fun loadMappings() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
        }
    }

    fun addMapping(mfgId: String, serialId: String) {
        viewModelScope.launch {
            val result = appendMappingUseCase.append(mfgId, serialId)
            if (result.isSuccess) {
                loadMappings()
            } else {
                _state.value = _state.value.copy(
                    errorMessage = result.exceptionOrNull()?.message ?: "登録失敗"
                )
            }
        }
    }
}