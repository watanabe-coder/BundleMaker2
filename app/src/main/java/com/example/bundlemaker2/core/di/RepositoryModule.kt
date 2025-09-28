package com.example.bundlemaker2.core.di

import com.example.bundlemaker2.data.repository.MfgSerialMappingRepository
import com.example.bundlemaker2.data.repository.MfgSerialMappingRepositoryImpl
import com.example.bundlemaker2.data.repository.MfgSerialRemoteRepository
import com.example.bundlemaker2.data.repository.MfgSerialRemoteRepositoryImpl
import com.example.bundlemaker2.data.repository.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * リポジトリの依存性を提供するHiltモジュール
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindMfgSerialMappingRepository(
        impl: MfgSerialMappingRepositoryImpl
    ): MfgSerialMappingRepository

    @Binds
    @Singleton
    fun bindMfgSerialRemoteRepository(
        impl: RemoteRepositoryImpl
    ): MfgSerialRemoteRepository
}
