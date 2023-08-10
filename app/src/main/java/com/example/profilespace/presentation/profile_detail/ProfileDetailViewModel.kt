package com.example.profilespace.presentation.profile_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profilespace.common.Resource
import com.example.profilespace.domain.model.Profile
import com.example.profilespace.domain.use_case.AddProfileUseCase
import com.example.profilespace.domain.use_case.DeleteProfileUseCase
import com.example.profilespace.domain.use_case.GetProfileUseCase
import com.example.profilespace.domain.use_case.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Created by Omar Elashry on 8/10/2023.
 */
@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val addProfileUseCase: AddProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _profile = MutableLiveData<Resource<Profile>>()
    private val _newProfile = MutableLiveData<Boolean>()
    private val _addState = MutableLiveData<Resource<Boolean>>()
    private val _updateState = MutableLiveData<Resource<Boolean>>()
    private val _deleteState = MutableLiveData<Resource<Boolean>>()
    val profile: LiveData<Resource<Profile>> get() = _profile
    val newProfile: LiveData<Boolean> get() = _newProfile
    val addState: LiveData<Resource<Boolean>> get() = _addState
    val updateState: LiveData<Resource<Boolean>> get() = _updateState
    val deleteState: LiveData<Resource<Boolean>> get() = _deleteState

    init {
        val profileId = savedStateHandle.get<Int>("profileId")
        if (profileId != -1) {
            _newProfile.value = false
            getProfile(profileId!!)
        } else {
            _newProfile.value = true
        }
    }

    private fun getProfile(id: Int) {
        getProfileUseCase.invoke(id).onEach { _profile.value = it }
            .launchIn(viewModelScope)
    }

    fun addProfile(profile: Profile) {
        addProfileUseCase.invoke(profile).onEach { _addState.value = it }
            .launchIn(viewModelScope)
    }

    fun updateProfile(profile: Profile) {
        updateProfileUseCase.invoke(profile).onEach { _updateState.value = it }
            .launchIn(viewModelScope)
    }

    fun deleteProfile(profile: Profile) {
        deleteProfileUseCase.invoke(profile).onEach { _deleteState.value = it }
            .launchIn(viewModelScope)
    }
}