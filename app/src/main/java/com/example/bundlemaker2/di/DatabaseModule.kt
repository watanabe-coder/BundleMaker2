package com.example.bundlemaker2.di

import android.content.Context
import com.example.bundlemaker2.data.database.AppDatabase
import com.example.bundlemaker2.data.dao.MfgSerialDao
import com.example.bundlemaker2.data.mapper.MappingMapper
import com.example.bundlemaker2.data.repository.MfgSerialRepositoryImpl
import com.example.bundlemaker2.domain.repository.MfgSerialRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideMfgSerialDao(database: AppDatabase): MfgSerialDao {
        return database.mfgSerialDao()
    }

    @Provides
    @Singleton
    fun provideMappingMapper(): MappingMapper {
        return MappingMapper()
    }

    @Provides
    @Singleton
    fun provideMfgSerialRepository(
        dao: MfgSerialDao,
        mapper: MappingMapper
    ): MfgSerialRepository {
        return MfgSerialRepositoryImpl(dao, mapper)
    }
}