package com.example.bundlemaker2.di

import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.local.dao.WorkSessionDao
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.data.mapper.SessionMapper
import com.example.bundlemaker2.data.repository.MappingRepositoryImpl
import com.example.bundlemaker2.data.repository.SessionRepositoryImpl
import com.example.bundlemaker2.domain.repository.MappingRepository
import com.example.bundlemaker2.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * リポジトリの依存性を提供するHiltモジュール
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMappingRepository(
        mappingDao: MfgSerialMappingDao,
        mapper: MappingMapper
    ): MappingRepository {
        return MappingRepositoryImpl(mappingDao, mapper)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(
        sessionDao: WorkSessionDao,
        mapper: SessionMapper
    ): SessionRepository {
        return SessionRepositoryImpl(sessionDao, mapper)
    }

    @Provides
    @Singleton
    fun provideMappingMapper(): MappingMapper {
        return MappingMapper
    }

    @Provides
    @Singleton
    fun provideSessionMapper(): SessionMapper {
        return SessionMapper
    }
}
