package com.example.bundlemaker2.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<UiState> : ViewModel() {
    protected val _uiState = MutableStateFlow(initialUiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    protected abstract fun initialUiState(): UiState
}