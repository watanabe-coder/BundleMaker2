package com.example.bundlemaker2.domain.usecase.impl

import com.example.bundlemaker2.domain.repository.MappingRepository
import com.example.bundlemaker2.domain.usecase.AppendMappingUseCase
import javax.inject.Inject

class AppendMappingUseCaseImpl @Inject constructor(
    private val mappingRepository: MappingRepository
) : AppendMappingUseCase {
    override suspend fun append(mfgId: String, serialId: String): Result<Unit> {
        return mappingRepository.appendMapping(mfgId, serialId)
    }
}