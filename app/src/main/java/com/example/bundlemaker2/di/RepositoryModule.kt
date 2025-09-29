package com.example.bundlemaker2.di

import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.data.repository.MfgSerialMappingRepositoryImpl
import com.example.bundlemaker2.domain.repository.MfgSerialMappingRepository
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
    fun provideMfgSerialMappingRepository(
        mappingDao: MfgSerialMappingDao,
        mapper: MappingMapper
    ): MfgSerialMappingRepository {
        return MfgSerialMappingRepositoryImpl(mappingDao, mapper)
    }

    @Provides
    @Singleton
    fun provideMappingMapper(): MappingMapper {
        return MappingMapper
    }
}
