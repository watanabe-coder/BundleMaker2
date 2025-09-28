package com.example.bundlemaker2.domain.usecase

import com.example.bundlemaker2.domain.model.MfgSerialMapping

interface GetMappingsUseCase {
    suspend fun getMappingsByMfgId(mfgId: String): List<MfgSerialMapping>
}