package com.example.bundlemaker2.data.repository

import com.example.bundlemaker2.data.local.dao.OutboxDao
import com.example.bundlemaker2.data.local.entity.Outbox
import com.example.bundlemaker2.data.local.entity.OutboxState
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

class OutboxRepositoryImpl @Inject constructor(
    private val dao: OutboxDao
) : OutboxRepository {

    override suspend fun insert(outbox: Outbox): Long = dao.insert(outbox)
    override suspend fun update(outbox: Outbox) = dao.update(outbox)
    override suspend fun delete(outbox: Outbox) = dao.delete(outbox)
    override fun getByState(state: OutboxState): Flow<List<Outbox>> = dao.getByState(state)
    override suspend fun updateState(outboxId: Long, state: OutboxState, lastTriedAt: Instant?) {
        dao.updateState(outboxId, state, lastTriedAt)
    }
    override suspend fun getById(outboxId: Long): Outbox? = dao.getById(outboxId)
}