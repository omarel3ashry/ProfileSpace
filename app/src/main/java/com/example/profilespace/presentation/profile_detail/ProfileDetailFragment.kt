package com.example.profilespace.presentation.profile_detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.profilespace.R
import com.example.profilespace.common.Resource
import com.example.profilespace.databinding.FragmentProfileDetailBinding
import com.example.profilespace.domain.model.Profile
import com.example.profilespace.presentation.MainActivity
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileDetailFragment : Fragment() {


    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileDetailViewModel by viewModels()
    private var profile: Profile? = null
    private var isNewProfile: Boolean = false
    private var inEditMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileDetailBinding.inflate(layoutInflater, container, false)
        (activity as MainActivity).setStatusBarColor(R.color.light_blue)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObservers()
        binding.deleteBtn.setOnClickListener { viewModel.deleteProfile(getProfile()) }
        binding.genderChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds[0] == binding.maleChip.id)
                populateGenderColors("Male")
            else populateGenderColors("Female")
        }
    }

    private fun setupViewModelObservers() {
        viewModel.profile.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    profile = resource.data!!
                    bindProfileData(profile!!)
                }

                Resource.Status.LOADING -> {
                }

                Resource.Status.ERROR -> {
                    Log.d(
                        "ProfileDetailFragment",
                        "setupViewModelObservers: ${resource.message}"
                    )
                }
            }
        }
        viewModel.newProfile.observe(viewLifecycleOwner) { isNew ->
            isNewProfile = isNew
            toggleEditMode(isNew)
            if (isNew)
                binding.toggleFAB.setOnClickListener { addProfile() }
            else
                binding.toggleFAB.setOnClickListener { updateProfile() }

        }
        viewModel.addState.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    findNavController().popBackStack()
                }

                Resource.Status.LOADING -> {
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(requireContext(), "${resource.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(
                        "ProfileDetailFragment",
                        "setupViewModelObservers: ${resource.message}"
                    )
                }
            }
        }
        viewModel.updateState.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    toggleEditMode(false)
                    Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT)
                        .show()
                }

                Resource.Status.LOADING -> {
                }

                Resource.Status.ERROR -> {
                    toggleEditMode(true)
                    Toast.makeText(requireContext(), "${resource.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(
                        "ProfileDetailFragment",
                        "setupViewModelObservers: ${resource.message}"
                    )
                }
            }
        }
        viewModel.deleteState.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    findNavController().popBackStack()
                }

                Resource.Status.LOADING -> {
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(requireContext(), "${resource.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(
                        "ProfileDetailFragment",
                        "setupViewModelObservers: ${resource.message}"
                    )
                }
            }
        }
    }

    // map profile object data to views
    private fun bindProfileData(profile: Profile) {
        binding.apply {
            nameET.setText(profile.name)
            jobTitleET.setText(profile.jobTitle)
            ageET.setText(profile.age.toString())
            countryET.setText(profile.country)
        }
        when (profile.level) {
            1 -> binding.juniorChip.isChecked = true
            2 -> binding.midLevelChip.isChecked = true
            3 -> binding.seniorChip.isChecked = true
            else -> binding.managerChip.isChecked = true
        }
        if (profile.gender == "Male")
            binding.maleChip.isChecked = true
        else binding.femaleChip.isChecked = true
    }

    private fun addProfile() {
        if (allDataIsSet()) {
            toggleEditMode(false)
            viewModel.addProfile(getProfile())
        }
    }

    private fun updateProfile() {
        if (!inEditMode){
            toggleEditMode(true)
        }
       else if (allDataIsSet()) {
            toggleEditMode(false)
            viewModel.updateProfile(getProfile())
        }
    }

    // map views data to profile object
    private fun getProfile(): Profile {
        binding.apply {
            val id = if (profile == null) 0 else profile!!.id
            val name = nameET.text.toString()
            val jobTitle = jobTitleET.text.toString()
            val age = ageET.text.toString().toInt()
            val country = countryET.text.toString()
            val level = when (levelChipGroup.checkedChipIds[0]) {
                juniorChip.id -> 1
                midLevelChip.id -> 2
                seniorChip.id -> 3
                else -> 4
            }
            var gender = "Male"
            if (femaleChip.isChecked)
                gender = "Female"
            return Profile(id, name, jobTitle, gender, age, level, country)
        }
    }

    // validating data
    private fun allDataIsSet(): Boolean {
        var emptyField = 0
        binding.apply {
            if (nameET.text.isNullOrEmpty()) {
                emptyField++
                nameET.error = "required."
            }
            if (jobTitleET.text.isNullOrEmpty()) {
                emptyField++
                jobTitleET.error = "required."
            }
            if (ageET.text.isNullOrEmpty()) {
                emptyField++
                ageET.error = "required."
            }
            if (countryET.text.isNullOrEmpty()) {
                emptyField++
                countryET.error = "required."
            }
        }
        return emptyField == 0
    }

    private fun toggleEditMode(editMode: Boolean) {
        inEditMode = editMode
        if (editMode) {
            binding.toggleFAB.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.done_ic
                )
            )
            binding.deleteBtn.visibility = View.INVISIBLE
        } else {
            binding.toggleFAB.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edit_ic
                )
            )
            binding.deleteBtn.visibility = View.VISIBLE
        }
        binding.apply {
            toggleEditText(nameET, editMode)
            toggleEditText(jobTitleET, editMode)
            toggleEditText(ageET, editMode)
            toggleEditText(countryET, editMode)
            toggleChipGroups(editMode)
        }

    }

    private fun toggleEditText(editText: TextInputEditText, editMode: Boolean) {
        if (!editMode) {
            editText.isEnabled = false
            editText.isFocusable = false
            editText.isFocusableInTouchMode = false
            editText.isCursorVisible = false
            editText.setTextColor(Color.BLACK)
            editText.setBackgroundColor(Color.TRANSPARENT)
        } else {
            editText.isEnabled = true
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
            editText.isCursorVisible = true
            editText.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_gray
                )
            )
        }
    }

    private fun toggleChipGroups(editMode: Boolean) {
        binding.levelChipGroup.children.iterator().forEach {
            it.isEnabled = editMode
        }
        binding.genderChipGroup.children.iterator().forEach {
            it.isEnabled = editMode
        }
    }

    // updating ui colors according to the selected gender
    private fun populateGenderColors(gender: String) {
        if (gender == "Male") {
            (activity as MainActivity).setStatusBarColor(R.color.light_blue)
            binding.bgView.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.curved_shape_blue)
            binding.genderIV.setImageResource(R.drawable.male_avatar)
        } else {
            (activity as MainActivity).setStatusBarColor(R.color.light_pink)
            binding.bgView.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.curved_shape_pink)
            binding.genderIV.setImageResource(R.drawable.female_avatar)
        }
    }

    // clean up the reference to the binding class instance
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}