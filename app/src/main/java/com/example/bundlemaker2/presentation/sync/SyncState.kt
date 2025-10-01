package com.example.bundlemaker2.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bundlemaker2.domain.usecase.SyncMfgSerialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SyncState {
    object Idle : SyncState()
    data class Loading(
        val current: Int = 0,
        val total: Int = 0
    ) : SyncState()

    data class Success(
        val message: String = "",
        val processedCount: Int = 0,
        val successCount: Int = 0,
        val failedCount: Int = 0
    ) : SyncState()

    data class Error(val message: String) : SyncState()
}

