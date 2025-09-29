package com.example.bundlemaker2.di

import com.example.bundlemaker2.data.dao.MfgSerialDao
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.data.repository.MfgSerialRepository
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
    fun provideMfgSerialRepository(
        mfgSerialDao: MfgSerialDao
    ): MfgSerialRepository {
        return MfgSerialRepository(mfgSerialDao)
    }

    @Provides
    @Singleton
    fun provideMappingMapper(): MappingMapper {
        return MappingMapper
    }
}
