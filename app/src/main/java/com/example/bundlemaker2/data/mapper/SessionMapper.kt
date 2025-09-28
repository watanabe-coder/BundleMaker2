package com.example.bundlemaker2.data.mapper

import com.example.bundlemaker2.data.local.entity.WorkSessionEntity
import com.example.bundlemaker2.domain.model.WorkSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 作業セッション関連のデータ変換を行うMapperクラス
 */
object SessionMapper {

    /**
     * ドメインモデルからエンティティに変換
     */
    fun toEntity(domain: WorkSession): WorkSessionEntity {
        return WorkSessionEntity(
            id = domain.id,
            mfgId = domain.mfgId,
            startedAt = domain.startedAt,
            endedAt = domain.endedAt,
            note = domain.note
        )
    }

    /**
     * エンティティからドメインモデルに変換
     */
    fun toDomain(entity: WorkSessionEntity): WorkSession {
        return WorkSession(
            id = entity.id,
            mfgId = entity.mfgId,
            startedAt = entity.startedAt,
            endedAt = entity.endedAt,
            note = entity.note
        )
    }

    /**
     * エンティティのリストからドメインモデルのリストに変換
     */
    fun toDomainList(entities: List<WorkSessionEntity>): List<WorkSession> {
        return entities.map { entity -> toDomain(entity) }
    }

    /**
     * エンティティのフローからドメインモデルのフローに変換
     */
    fun toDomainFlow(entities: Flow<List<WorkSessionEntity>>): Flow<List<WorkSession>> {
        return entities.map { entityList -> entityList.map { entity -> toDomain(entity) } }
    }
}
