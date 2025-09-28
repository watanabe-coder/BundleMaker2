package com.example.bundlemaker2.di

import android.content.Context
import com.example.bundlemaker2.data.database.AppDatabase
import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
import com.example.bundlemaker2.data.local.dao.WorkSessionDao
import com.example.bundlemaker2.data.local.dao.OutboxDao
import com.example.bundlemaker2.domain.repository.WorkSessionRepository
import com.example.bundlemaker2.data.repository.WorkSessionRepositoryImpl
import com.example.bundlemaker2.domain.repository.OutboxRepository
import com.example.bundlemaker2.data.repository.OutboxRepositoryImpl
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideWorkSessionDao(database: AppDatabase): WorkSessionDao {
        return database.workSessionDao()
    }

    @Provides
    fun provideOutboxDao(database: AppDatabase): OutboxDao {
        return database.outboxDao()
    }

    @Provides
    fun provideMfgSerialMappingDao(database: AppDatabase): MfgSerialMappingDao {
        return database.mfgSerialMappingDao()
    }

    @Singleton
    @Provides
    fun provideWorkSessionRepository(dao: WorkSessionDao): WorkSessionRepository {
        return WorkSessionRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideOutboxRepository(dao: OutboxDao): OutboxRepository {
        return OutboxRepositoryImpl(dao)
    }
}