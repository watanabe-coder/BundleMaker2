package com.example.bundlemaker2.domain.usecase.impl

import com.example.bundlemaker2.domain.model.MfgSerialMapping
import com.example.bundlemaker2.domain.repository.MappingRepository
import com.example.bundlemaker2.domain.usecase.GetMappingsUseCase
import javax.inject.Inject

class GetMappingsUseCaseImpl @Inject constructor(
    private val mappingRepository: MappingRepository
) : GetMappingsUseCase {
    override suspend fun getMappingsByMfgId(mfgId: String): List<MfgSerialMapping> {
        return mappingRepository.getMappingsByMfgId(mfgId)
    }
}