package com.example.profilespace.di

import com.example.profilespace.data.local.ProfileDao
import com.example.profilespace.data.repository.ProfileRepositoryImpl
import com.example.profilespace.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Omar Elashry on 8/9/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    // Better practice creating repository this way so we can mock it in testing.
    fun provideProfileRepository(profileDao: ProfileDao): ProfileRepository {
        return ProfileRepositoryImpl(profileDao)
    }
}