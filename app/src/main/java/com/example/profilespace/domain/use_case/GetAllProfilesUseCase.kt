package com.example.profilespace.domain.use_case

import com.example.profilespace.data.local.entities.toProfile
import com.example.profilespace.domain.model.Profile
import com.example.profilespace.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by Omar Elashry on 8/9/2023.
 */
class GetAllProfilesUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(): Flow<List<Profile>> = flow {
        profileRepository.getProfiles().flowOn(Dispatchers.IO).collect { entities ->
            val profiles = entities.map { it.toProfile() }
            emit(profiles)
        }
    }
}