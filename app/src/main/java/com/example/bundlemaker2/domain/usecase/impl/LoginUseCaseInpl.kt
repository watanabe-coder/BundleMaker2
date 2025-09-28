package com.example.bundlemaker2.domain.usecase.impl

import com.example.bundlemaker2.domain.repository.AuthRepository
import com.example.bundlemaker2.domain.usecase.LoginUseCase
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LoginUseCase {
    override suspend fun login(userId: String, password: String): Result<Unit> {
        return authRepository.login(userId, password)
    }
}