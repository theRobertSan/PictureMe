package com.example.pictureme.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import coil.load
import com.example.pictureme.R
import com.example.pictureme.data.Response
import com.example.pictureme.databinding.FragmentProfileBinding
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setUserData()
        setLayoutClickListeners()

        return (binding.root)
    }

    private fun setUserData() {

        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            binding.textName.text = user.username

            // Check whether the user has a profile picture or not
            if (user.profilePicturePath == null) {
                binding.imageProfilePicture.setImageResource(R.drawable.default_profile_picture)
                binding.imageLoadingBar.isGone = true
            } else {
                binding.imageProfilePicture.load(user.profilePicturePath) {
                    crossfade(true)
                    crossfade(1000)
                    listener { _, _ ->
                        binding.imageLoadingBar.isGone = true
                    }
                }
            }


            //binding.imageProfilePicture.image = user.picture

            // Get num of Friends
            binding.textFriendsNum.text = user.friendships!!.size.toString()
        }

        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { picmes ->
            // Get num of PicMes
            binding.textPicmeNum.text = picmes.size.toString()
        }
    }

    private fun setLayoutClickListeners() {

        binding.friendsLayout.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_profileFragment_to_friendsFragment)
        }

        binding.editLayout.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_profileFragment_to_editProfile)
        }

        binding.copyLayout.setOnClickListener {
            Toast.makeText(this.context, "Copy Fragment!", Toast.LENGTH_SHORT).show()
        }

        binding.settingsLayout.setOnClickListener {
            Toast.makeText(this.context, "Settings Fragment!", Toast.LENGTH_SHORT).show()
        }

        binding.logoutLayout.setOnClickListener {
            Toast.makeText(this.context, "Logout Fragment!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}