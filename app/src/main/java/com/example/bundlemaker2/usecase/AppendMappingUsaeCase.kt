package com.example.bundlemaker2.usecase

interface AppendMappingUseCase {
    suspend fun append(mfgId: String, serialId: String): Result<Unit>
}