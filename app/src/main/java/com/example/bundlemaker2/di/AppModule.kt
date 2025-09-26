package com.example.bundlemaker2.di

import android.content.Context
import com.example.bundlemaker2.data.database.AppDatabase
import com.example.bundlemaker2.data.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.dao.WorkSessionDao
import com.example.bundlemaker2.data.repository.MfgSerialRepository
import com.example.bundlemaker2.data.repository.MfgSerialRepositoryImpl
import com.example.bundlemaker2.data.repository.WorkSessionRepository
import com.example.bundlemaker2.data.repository.WorkSessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideMfgSerialMappingDao(database: AppDatabase): MfgSerialMappingDao {
        return database.mfgSerialMappingDao()
    }

    @Provides
    fun provideWorkSessionDao(database: AppDatabase): WorkSessionDao {
        return database.workSessionDao()
    }

    @Singleton
    @Provides
    fun provideMfgSerialRepository(dao: MfgSerialMappingDao): MfgSerialRepository {
        return MfgSerialRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideWorkSessionRepository(dao: WorkSessionDao): WorkSessionRepository {
        return WorkSessionRepositoryImpl(dao)
    }
}