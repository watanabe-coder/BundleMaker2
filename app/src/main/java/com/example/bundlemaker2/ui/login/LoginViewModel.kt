package com.example.bundlemaker2.ui.login

import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val employeeId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<LoginUiState>() {

    override fun initialUiState(): LoginUiState = LoginUiState()

    fun onEmployeeIdChanged(employeeId: String) {
        _uiState.update { it.copy(employeeId = employeeId) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // ここにログイン処理を実装
            // 例: authRepository.login(employeeId, password)

            // ログイン成功時
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoginSuccess = true
                )
            }
        }
    }
}