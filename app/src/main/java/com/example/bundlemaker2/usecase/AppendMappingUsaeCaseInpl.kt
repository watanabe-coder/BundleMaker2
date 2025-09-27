package com.example.bundlemaker2.usecase

import com.example.bundlemaker2.data.repository.MappingRepository
import javax.inject.Inject

class AppendMappingUseCaseImpl @Inject constructor(
    private val mappingRepository: MappingRepository
) : AppendMappingUseCase {
    override suspend fun append(mfgId: String, serialId: String): Result<Unit> {
        return mappingRepository.appendMapping(mfgId, serialId)
    }
}