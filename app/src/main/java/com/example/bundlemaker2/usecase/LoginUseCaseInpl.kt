package com.example.bundlemaker2.usecase

import com.example.bundlemaker2.data.repository.AuthRepository
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LoginUseCase {
    override suspend fun login(userId: String, password: String): Result<Unit> {
        return authRepository.login(userId, password)
    }
}