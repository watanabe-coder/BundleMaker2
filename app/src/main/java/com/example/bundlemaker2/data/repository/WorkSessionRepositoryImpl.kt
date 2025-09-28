package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.local.dao.WorkSessionDao
import com.example.bundlemaker2.data.mapper.SessionMapper
import com.example.bundlemaker2.domain.model.WorkSession
import com.example.bundlemaker2.domain.repository.WorkSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class WorkSessionRepositoryImpl @Inject constructor(
    private val dao: WorkSessionDao,
    private val mapper: SessionMapper
) : WorkSessionRepository {
    override suspend fun insert(session: WorkSession): Long {
        return dao.insert(mapper.toEntity(session))
    }

    override suspend fun update(session: WorkSession) {
        dao.update(mapper.toEntity(session))
    }

    override suspend fun delete(session: WorkSession) {
        dao.delete(mapper.toEntity(session))
    }

    override fun getSessionsByMfgId(mfgId: String): Flow<List<WorkSession>> {
        return dao.getByMfgId(mfgId).map { entities ->
            entities.map { mapper.toDomain(it) }
        }
    }

    override fun getAllSessions(): Flow<List<WorkSession>> {
        return dao.observeAll().map { entities ->
            entities.map { mapper.toDomain(it) }
        }
    }

    override suspend fun getById(id: Long): WorkSession? {
        return dao.getById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun getLatestByMfgId(mfgId: String): WorkSession? {
        return dao.getByMfgId(mfgId)
            .first() // Flowから最初の値を取得
            .firstOrNull() // リストの最初の要素を取得
            ?.let { mapper.toDomain(it) } // ドメインモデルに変換
    }

    override fun getActiveSessions(): Flow<List<WorkSession>> {
        return dao.observeAll().map { entities ->
            entities.filter { it.endedAt == null }.map { mapper.toDomain(it) }
        }
    }

    override suspend fun endSession(sessionId: Long, endTime: Instant) {
        dao.endSession(sessionId, endTime)
    }

    override suspend fun count(): Int {
        return dao.observeAll().first().size
    }
}