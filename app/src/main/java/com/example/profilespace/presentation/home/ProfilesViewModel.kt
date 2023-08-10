package com.example.profilespace.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profilespace.domain.model.Profile
import com.example.profilespace.domain.use_case.DeleteProfileUseCase
import com.example.profilespace.domain.use_case.GetAllProfilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Created by Omar Elashry on 8/10/2023.
 */
@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val getAllProfilesUseCase: GetAllProfilesUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    init {
        getProfiles()
    }

    private fun getProfiles() {
        getAllProfilesUseCase.invoke().onEach { _profiles.value = it }.launchIn(viewModelScope)
    }

    fun deleteProfile(profile: Profile) {
        deleteProfileUseCase.invoke(profile).launchIn(viewModelScope)
    }

}