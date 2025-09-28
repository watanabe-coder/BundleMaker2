package com.example.bundlemaker2.data.mapper

import com.example.bundlemaker2.data.local.entity.MfgSerialMappingEntity
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.model.MfgSerialMapping
import java.time.Instant

object MappingMapper {
    fun toEntity(domain: MfgSerialMapping): MfgSerialMappingEntity {
        return MfgSerialMappingEntity(
            id = domain.id,
            mfgId = domain.mfgId,
            serialId = domain.serialId,
            scannedAt = domain.scannedAt,
            status = domain.status.name,
            errorCode = domain.errorCode
        )
    }

    fun toDomain(entity: MfgSerialMappingEntity): MfgSerialMapping {
        return MfgSerialMapping(
            id = entity.id,
            mfgId = entity.mfgId,
            serialId = entity.serialId,
            scannedAt = entity.scannedAt,
            status = MappingStatus.valueOf(entity.status),
            errorCode = entity.errorCode
        )
    }

    fun toDomainList(entities: List<MfgSerialMappingEntity>): List<MfgSerialMapping> {
        return entities.map { toDomain(it) }
    }
}