package com.example.bundlemaker2.data.mapper

import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import java.time.Instant
import javax.inject.Inject

class MappingMapper @Inject constructor() {
    fun toEntity(domain: MfgSerialMapping): MfgSerialMappingEntity {
        return MfgSerialMappingEntity(
            id = domain.id,
            mfgId = domain.mfgId,
            serialId = domain.serialId,
            status = domain.status.name,
            synced = domain.synced
        )
    }

    fun toDomain(entity: MfgSerialMappingEntity): MfgSerialMapping {
        return MfgSerialMapping(
            id = entity.id,
            mfgId = entity.mfgId,
            serialId = entity.serialId,
            status = MappingStatus.valueOf(entity.status),
            scannedAt = Instant.now(), // or use a default value from the entity if available
            synced = entity.synced
        )
    }
}