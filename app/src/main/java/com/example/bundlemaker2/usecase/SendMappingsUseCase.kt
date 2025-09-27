package com.example.bundlemaker2.usecase

import com.example.bundlemaker2.data.entity.MappingStatus
import com.example.bundlemaker2.data.repository.MfgSerialMappingRepository
import com.example.bundlemaker2.data.repository.OutboxRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SendMappingsUseCase @Inject constructor(
    private val mappingRepository: MfgSerialMappingRepository,
    private val outboxRepository: OutboxRepository
) {
    suspend fun sendMappings(mfgId: String): Result<Unit> {
        // READY状態のマッピングを取得
        val readyMappings = mappingRepository
            .getByMfgIdAndStatuses(mfgId, listOf(MappingStatus.READY))
            .first()
        // OutboxRepositoryの送信メソッドに合わせて修正
        // 例: outboxRepository.enqueueMappings(mfgId, readyMappings)
        return Result.success(Unit) // 実装に合わせて修正
    }
}