package com.example.profilespace.domain.repository

import com.example.profilespace.data.local.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Omar Elashry on 8/9/2023.
 */
interface ProfileRepository {

    suspend fun getProfileById(id: Int): ProfileEntity
    fun getProfiles(): Flow<List<ProfileEntity>>
    suspend fun addProfile(profileEntity: ProfileEntity): Long
    suspend fun updateProfile(profileEntity: ProfileEntity): Long
    suspend fun deleteProfile(profileEntity: ProfileEntity): Int
}