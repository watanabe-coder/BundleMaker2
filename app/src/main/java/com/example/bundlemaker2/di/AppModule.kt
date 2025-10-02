package com.example.bundlemaker2.di

import android.content.Context
import com.example.bundlemaker2.data.database.AppDatabase
import com.example.bundlemaker2.data.api.SyncService
import com.example.bundlemaker2.data.dao.MfgSerialDao
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.data.mapper.SyncDataMapper
import com.example.bundlemaker2.data.repository.MfgSerialRepositoryImpl
import com.example.bundlemaker2.domain.repository.MfgSerialRepository
import com.example.bundlemaker2.domain.usecase.SyncMfgSerialsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMfgSerialRepository(
        dao: MfgSerialDao,
        mapper: MappingMapper
    ): MfgSerialRepository {
        return MfgSerialRepositoryImpl(dao, mapper)
    }

    @Provides
    @Singleton
    fun provideSyncMfgSerialsUseCase(
        repository: MfgSerialRepository,
        syncService: SyncService,
        mapper: SyncDataMapper
    ): SyncMfgSerialsUseCase {
        return SyncMfgSerialsUseCase(repository, syncService, mapper)
    }

    @Provides
    @Singleton
    fun provideMappingMapper(): MappingMapper {
        return MappingMapper()
    }

    @Provides
    @Singleton
    fun provideSyncDataMapper(): SyncDataMapper {
        return SyncDataMapper()
    }
}