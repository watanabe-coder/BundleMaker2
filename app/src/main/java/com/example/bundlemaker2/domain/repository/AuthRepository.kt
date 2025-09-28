package com.example.bundlemaker2.domain.repository

interface AuthRepository {
    suspend fun login(userId: String, password: String): Result<Unit>
}