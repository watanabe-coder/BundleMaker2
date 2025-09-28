package com.example.bundlemaker2.domain.usecase

interface AppendMappingUseCase {
    suspend fun append(mfgId: String, serialId: String): Result<Unit>
}