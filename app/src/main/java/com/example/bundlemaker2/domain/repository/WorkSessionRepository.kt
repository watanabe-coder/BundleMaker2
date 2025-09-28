package com.example.bundlemaker2.domain.repository

import com.example.bundlemaker2.domain.model.WorkSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface WorkSessionRepository {
    suspend fun insert(session: WorkSession): Long
    suspend fun update(session: WorkSession)
    suspend fun delete(session: WorkSession)
    fun getSessionsByMfgId(mfgId: String): Flow<List<WorkSession>>
    fun getAllSessions(): Flow<List<WorkSession>>
    suspend fun getById(id: Long): WorkSession?
    suspend fun getLatestByMfgId(mfgId: String): WorkSession?
    fun getActiveSessions(): Flow<List<WorkSession>>
    suspend fun endSession(sessionId: Long, endTime: Instant)
    suspend fun count(): Int
}