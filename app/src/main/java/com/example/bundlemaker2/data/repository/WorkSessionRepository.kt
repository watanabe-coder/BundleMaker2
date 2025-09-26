package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.entity.WorkSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * 作業セッションを管理するリポジトリインターフェース
 */
interface WorkSessionRepository {
    // セッションをIDで取得
    suspend fun getSession(id: Long): WorkSession?
    
    // 製造番号に紐づく最新のセッションを取得
    suspend fun getLatestSessionByMfgId(mfgId: String): WorkSession?
    
    // すべてのセッションを取得
    fun getAllSessions(): Flow<List<WorkSession>>
    
    // アクティブなセッションを取得（終了していないセッション）
    fun getActiveSessions(): Flow<List<WorkSession>>
    
    // 新しいセッションを開始
    suspend fun startSession(mfgId: String, note: String? = null): Result<Long>
    
    // セッションを終了
    suspend fun endSession(sessionId: Long, endTime: Instant = Instant.now()): Result<Unit>
    
    // セッションを更新
    suspend fun updateSession(session: WorkSession): Result<Unit>
    
    // セッションを削除
    suspend fun deleteSession(session: WorkSession): Result<Unit>
    
    // セッション数を取得
    suspend fun count(): Int
}
