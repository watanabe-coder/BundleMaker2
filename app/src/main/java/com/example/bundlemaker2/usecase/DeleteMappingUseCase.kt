package com.example.bundlemaker2.usecase

interface DeleteMappingUseCase {
    suspend fun delete(mappingId: Long): Result<Unit>
}