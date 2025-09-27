package com.example.bundlemaker2.data.repository

interface AuthRepository {
    suspend fun login(userId: String, password: String): Result<Unit>
}