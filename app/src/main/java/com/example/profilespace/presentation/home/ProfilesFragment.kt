package com.example.profilespace.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profilespace.R
import com.example.profilespace.databinding.FragmentProfilesBinding
import com.example.profilespace.domain.model.Profile
import com.example.profilespace.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfilesFragment : Fragment() {

    private var _binding: FragmentProfilesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfilesViewModel by viewModels()
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfilesBinding.inflate(layoutInflater, container, false)
        (activity as MainActivity).setStatusBarColor(R.color.light_gray)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // resetting status bar color again
        (activity as MainActivity).setStatusBarColor(R.color.light_gray)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileRecView()
        setupViewModelObservers()
        binding.addProfileFAB.setOnClickListener {
            // navigate to ProfileDetailFragment without profileId to create new profile
            findNavController().navigate(R.id.action_profilesFragment_to_profileDetailFragment)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.profiles.observe(viewLifecycleOwner) { profiles ->
            if (profiles != null) {
                profileAdapter.submitList(profiles)
                if (profiles.isNotEmpty()) {
                    binding.noProfilesTV.visibility = View.GONE
                } else binding.noProfilesTV.visibility = View.VISIBLE
            } else Log.d("ProfilesFragment", "unexpected error!")
        }
    }

    private fun setupProfileRecView() {
        binding.profileRV.layoutManager = LinearLayoutManager(requireContext())
        profileAdapter = ProfileAdapter(object : ItemListener {
            override fun onClick(item: Profile, position: Int) {
                // navigate to ProfileDetailFragment profileId to view, edit, or delete.
                val action =
                    ProfilesFragmentDirections.actionProfilesFragmentToProfileDetailFragment(item.id)
                findNavController().navigate(action)
            }

            override fun onDelete(item: Profile, position: Int) {
                // delete selected profile within the list
                viewModel.deleteProfile(item)
            }
        })
        binding.profileRV.adapter = profileAdapter
    }

    // clean up the reference to the binding class instance
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}