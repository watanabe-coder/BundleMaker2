package com.example.bundlemaker2.usecase

import com.example.bundlemaker2.data.repository.MappingRepository
import javax.inject.Inject

class ConfirmMappingsUseCaseImpl @Inject constructor(
    private val mappingRepository: MappingRepository
) : ConfirmMappingsUseCase {
    override suspend fun confirm(mfgId: String): Result<Unit> {
        // MFGに紐づく全Mappingを「確定」状態に更新
        return mappingRepository.confirmMappings(mfgId)
    }

    override suspend fun deleteMapping(mappingId: Long): Result<Unit> {
        // 指定IDのMappingを削除
        return mappingRepository.deleteMapping(mappingId)
    }
}