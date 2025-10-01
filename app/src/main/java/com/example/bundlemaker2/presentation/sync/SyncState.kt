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
    object Idle : SyncState()         // 初期状態
    object Loading : SyncState()      // ローディング中
    data class Success(val message: String) : SyncState()  // 成功
    data class Error(val message: String) : SyncState()    // エラー
    data class Progress(val current: Int, val total: Int) : SyncState() // 進捗状況
}

