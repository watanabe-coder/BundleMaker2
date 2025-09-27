package com.example.bundlemaker2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.usecase.SendMappingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SendMappingsViewModel @Inject constructor(
    private val sendMappingsUseCase: SendMappingsUseCase
) : ViewModel() {

    private val _sendResult = MutableStateFlow<Result<Unit>?>(null)
    val sendResult: StateFlow<Result<Unit>?> = _sendResult

    fun sendMappings(mfgId: String) {
        viewModelScope.launch {
            val result = sendMappingsUseCase.sendMappings(mfgId)
            _sendResult.value = result
        }
    }
}