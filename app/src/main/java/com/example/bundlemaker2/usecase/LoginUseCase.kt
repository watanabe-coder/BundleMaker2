package com.example.bundlemaker2.usecase

interface LoginUseCase {
    suspend fun login(userId: String, password: String): Result<Unit>
}