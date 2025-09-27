package com.example.bundlemaker2.usecase

interface ConfirmMappingsUseCase {
    suspend fun confirm(mfgId: String): Result<Unit>
    suspend fun deleteMapping(mappingId: Long): Result<Unit>
}