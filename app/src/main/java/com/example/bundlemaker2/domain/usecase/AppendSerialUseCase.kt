package com.example.bundlemaker2.domain.usecase

import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.model.MappingStatus
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository
import javax.inject.Inject
import java.time.Instant

class AppendSerialUseCase @Inject constructor(
    private val mappingRepository: MfgSerialMappingRepository
) {
    suspend fun appendSerial(mfgId: String, serialId: String): Result<Long> {
        val mapping = MfgSerialMapping(
            id = 0L,
            mfgId = mfgId,
            serialId = serialId,
            scannedAt = Instant.now(),
            status = MappingStatus.DRAFT,
            errorCode = null
        )
        return try {
            val id = mappingRepository.insert(mapping)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}