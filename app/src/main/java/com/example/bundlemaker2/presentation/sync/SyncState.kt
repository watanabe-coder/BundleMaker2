package com.example.bundlemaker2.presentation.sync

sealed class SyncState {
    object Idle : SyncState()         // 初期状態
    object Loading : SyncState()      // ローディング中
    data class Success(val message: String) : SyncState()  // 成功
    data class Error(val message: String) : SyncState()    // エラー
    data class Progress(val current: Int, val total: Int) : SyncState() // 進捗状況
}