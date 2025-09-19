package com.example.bundlemaker2.domain.usecase

import com.example.bundlemaker2.domain.entity.MfgSerialMapping
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository

class AppendMappingUseCase(
    private val repository: MfgSerialMappingRepository
) {
    suspend operator fun invoke(mapping: MfgSerialMapping) {
        repository.insert(mapping)
    }
}