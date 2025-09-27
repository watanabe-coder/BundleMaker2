package com.example.bundlemaker2.usecase

import com.example.bundlemaker2.data.repository.MappingRepository
import javax.inject.Inject

class DeleteMappingUseCaseImpl @Inject constructor(
    private val mappingRepository: MappingRepository
) : DeleteMappingUseCase {
    override suspend fun delete(mappingId: Long): Result<Unit> {
        return mappingRepository.deleteMapping(mappingId)
    }
}