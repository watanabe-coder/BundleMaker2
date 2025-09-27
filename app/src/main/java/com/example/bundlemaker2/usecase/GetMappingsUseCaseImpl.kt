package com.example.bundlemaker2.usecase

import com.example.bundlemaker2.data.entity.MfgSerialMapping
import com.example.bundlemaker2.data.repository.MappingRepository
import javax.inject.Inject

class GetMappingsUseCaseImpl @Inject constructor(
    private val mappingRepository: MappingRepository
) : GetMappingsUseCase {
    override suspend fun getMappingsByMfgId(mfgId: String): List<MfgSerialMapping> {
        return mappingRepository.getMappingsByMfgId(mfgId)
    }
}