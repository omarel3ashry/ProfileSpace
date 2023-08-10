package com.example.profilespace.data.repository

import com.example.profilespace.data.local.ProfileDao
import com.example.profilespace.data.local.entities.ProfileEntity
import com.example.profilespace.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Omar Elashry on 8/9/2023.
 */

// Better practice creating repository this way so we can mock it in testing.
class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao
) : ProfileRepository {

    override suspend fun getProfileById(id: Int): ProfileEntity = profileDao.getProfile(id)

    override fun getProfiles(): Flow<List<ProfileEntity>> = profileDao.getProfiles()

    override suspend fun addProfile(profileEntity: ProfileEntity): Long =
        profileDao.insertProfile(profileEntity)

    override suspend fun updateProfile(profileEntity: ProfileEntity): Long =
        profileDao.insertProfile(profileEntity)

    override suspend fun deleteProfile(profileEntity: ProfileEntity): Int =
        profileDao.deleteProfile(profileEntity)
}