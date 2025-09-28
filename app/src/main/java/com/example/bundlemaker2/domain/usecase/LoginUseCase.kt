package com.example.bundlemaker2.domain.usecase

interface LoginUseCase {
    suspend fun login(userId: String, password: String): Result<Unit>
}