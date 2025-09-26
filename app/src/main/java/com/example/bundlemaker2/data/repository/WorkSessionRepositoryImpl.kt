package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.dao.WorkSessionDao
import com.example.bundlemaker2.data.entity.WorkSession
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkSessionRepositoryImpl @Inject constructor(
    private val workSessionDao: WorkSessionDao
) : WorkSessionRepository {

    override suspend fun getSession(id: Long): WorkSession? {
        return try {
            workSessionDao.getById(id)
        } catch (e: Exception) {
            Result.failure<WorkSession>(e)
            null
        }
    }

    override suspend fun getLatestSessionByMfgId(mfgId: String): WorkSession? {
        return try {
            workSessionDao.getLatestByMfgId(mfgId)
        } catch (e: Exception) {
            Result.failure<WorkSession>(e)
            null
        }
    }

    override fun getAllSessions(): Flow<List<WorkSession>> {
        return workSessionDao.getAll()
    }

    override fun getActiveSessions(): Flow<List<WorkSession>> {
        return workSessionDao.getActiveSessions()
    }

    override suspend fun startSession(mfgId: String, note: String?): Result<Long> {
        return try {
            // 同じ製造番号で未完了のセッションがあるか確認
            val activeSession = workSessionDao.getLatestByMfgId(mfgId)?.takeIf { it.endedAt == null }
            if (activeSession != null) {
                return Result.failure(Exception("この製造番号のセッションは既に開始されています"))
            }
            
            val session = WorkSession(
                mfgId = mfgId,
                note = note,
                startedAt = Instant.now(),
                endedAt = null
            )
            val id = workSessionDao.insert(session)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun endSession(sessionId: Long, endTime: Instant): Result<Unit> {
        return try {
            workSessionDao.endSession(sessionId, endTime)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSession(session: WorkSession): Result<Unit> {
        return try {
            workSessionDao.update(session)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSession(session: WorkSession): Result<Unit> {
        return try {
            workSessionDao.delete(session)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun count(): Int {
        return try {
            workSessionDao.count()
        } catch (e: Exception) {
            -1
        }
    }
}
