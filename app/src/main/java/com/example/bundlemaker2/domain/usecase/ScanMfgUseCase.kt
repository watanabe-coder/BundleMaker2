package com.example.bundlemaker2.domain.usecase

import com.example.bundlemaker2.domain.model.WorkSession
import com.example.bundlemaker2.domain.repository.WorkSessionRepository
import java.time.Instant
import javax.inject.Inject

class ScanMfgUseCase @Inject constructor(
    private val workSessionRepository: WorkSessionRepository
) {
    suspend fun startSession(mfgId: String): Long {
        val session = WorkSession(
            id = 0L,
            mfgId = mfgId,
            startedAt = Instant.now(),
            endedAt = null,
            note = null
        )
        return workSessionRepository.insert(session)
    }
}