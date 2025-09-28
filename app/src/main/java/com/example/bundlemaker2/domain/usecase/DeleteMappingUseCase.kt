package com.example.bundlemaker2.domain.usecase

interface DeleteMappingUseCase {
    suspend fun delete(mappingId: Long): Result<Unit>
}