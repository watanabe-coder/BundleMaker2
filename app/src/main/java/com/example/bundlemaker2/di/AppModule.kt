package com.example.bundlemaker2.di

import android.content.Context
import com.example.bundlemaker2.data.database.AppDatabase
import com.example.bundlemaker2.data.local.dao.MfgSerialMappingDao
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
    fun provideMfgSerialMappingDao(database: AppDatabase): MfgSerialMappingDao {
        return database.mfgSerialMappingDao()
    }
}