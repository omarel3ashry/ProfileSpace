package com.example.profilespace.domain.use_case

import com.example.profilespace.common.Resource
import com.example.profilespace.domain.model.Profile
import com.example.profilespace.domain.model.toProfileEntity
import com.example.profilespace.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Omar Elashry on 8/9/2023.
 */
class DeleteProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(profile: Profile): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading(false))
        try {
            // validate using the deletedRows count, must be 1
            val deletedRow = profileRepository.deleteProfile(profile.toProfileEntity())
            emit(Resource.success(deletedRow == 1))
        } catch (e: Exception) {
            emit(Resource.error(false, e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}