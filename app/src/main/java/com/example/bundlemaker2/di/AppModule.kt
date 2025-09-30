package com.example.bundlemaker2.di

import android.content.Context
import androidx.room.Room
import com.example.bundlemaker2.data.database.AppDatabase
import com.example.bundlemaker2.data.dao.MfgSerialDao
import com.example.bundlemaker2.data.mapper.MappingMapper
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
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bundle_maker.db"
        ).build()
    }

    @Provides
    fun provideMfgSerialDao(database: AppDatabase): MfgSerialDao {
        return database.mfgSerialDao()
    }

    @Provides
    @Singleton
    fun provideMappingMapper(): MappingMapper = MappingMapper()
}