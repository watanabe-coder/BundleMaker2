package com.example.bundlemaker2.domain.usecase.impl

import com.example.bundlemaker2.domain.repository.MappingRepository
import com.example.bundlemaker2.domain.usecase.DeleteMappingUseCase
import javax.inject.Inject

class DeleteMappingUseCaseImpl @Inject constructor(
    private val mappingRepository: MappingRepository
) : DeleteMappingUseCase {
    override suspend fun delete(mappingId: Long): Result<Unit> {
        return mappingRepository.deleteMapping(mappingId)
    }
}