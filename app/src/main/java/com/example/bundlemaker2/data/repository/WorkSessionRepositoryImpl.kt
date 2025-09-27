package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.dao.WorkSessionDao
import com.example.bundlemaker2.data.entity.WorkSession
import com.example.bundlemaker2.data.repository.WorkSessionRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

class WorkSessionRepositoryImpl @Inject constructor(
    private val dao: WorkSessionDao
) : WorkSessionRepository {
    override suspend fun insert(session: WorkSession): Long = dao.insert(session)
    override suspend fun update(session: WorkSession) = dao.update(session)
    override suspend fun delete(session: WorkSession) = dao.delete(session)
    override fun getSessionsByMfgId(mfgId: String): Flow<List<WorkSession>> = dao.getSessionsByMfgId(mfgId)
    override fun getAllSessions(): Flow<List<WorkSession>> = dao.getAllSessions()
    override suspend fun getById(id: Long): WorkSession? = dao.getById(id)
    override suspend fun getLatestByMfgId(mfgId: String): WorkSession? = dao.getLatestByMfgId(mfgId)
    override fun getActiveSessions(): Flow<List<WorkSession>> = dao.getActiveSessions()
    override suspend fun endSession(sessionId: Long, endTime: Instant) = dao.endSession(sessionId, endTime)
    override suspend fun count(): Int = dao.count()
}