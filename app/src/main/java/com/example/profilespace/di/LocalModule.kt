package com.example.profilespace.di

import android.content.Context
import androidx.room.Room
import com.example.profilespace.data.local.ProfileDao
import com.example.profilespace.data.local.ProfileDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Omar Elashry on 8/9/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideProfileDatabase(@ApplicationContext context: Context): ProfileDatabase {
        return Room.databaseBuilder(context, ProfileDatabase::class.java, ProfileDatabase.DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideProfileDao(profileDatabase: ProfileDatabase): ProfileDao {
        return profileDatabase.profileDao()
    }
}