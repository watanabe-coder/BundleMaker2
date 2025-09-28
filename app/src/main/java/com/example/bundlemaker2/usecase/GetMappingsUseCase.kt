package com.example.bundlemaker2.usecase

import com.example.bundlemaker2.data.local.entity.MfgSerialMapping

interface GetMappingsUseCase {
    suspend fun getMappingsByMfgId(mfgId: String): List<MfgSerialMapping>
}