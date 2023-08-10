package com.example.profilespace.domain.use_case

import com.example.profilespace.common.Resource
import com.example.profilespace.data.local.entities.toProfile
import com.example.profilespace.domain.model.Profile
import com.example.profilespace.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Omar Elashry on 8/9/2023.
 */
class GetProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    operator fun invoke(id: Int): Flow<Resource<Profile>> = flow {
        try {
            val profile = profileRepository.getProfileById(id).toProfile()
            emit(Resource.success(profile))
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}