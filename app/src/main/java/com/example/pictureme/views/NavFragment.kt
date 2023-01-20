package com.example.pictureme.views

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentNavBinding
import com.example.pictureme.utils.Permissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavFragment : Fragment() {

    private var _binding: FragmentNavBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavBinding.inflate(inflater, container, false);

        binding.buttonCamera.setOnClickListener {
            setupCameraButton()
        }

        setupBottomNav()

        return (binding.root)
    }

    private fun setupBottomNav() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = binding.bottomNavView
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        setupWithNavController(bottomNavigationView, navController)
    }

    private fun setupCameraButton() {

        // If required permissions enabled, do something
        Permissions.checkPermissions(
            requireContext(),
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            "To take a PicMe, you have to enable those permissions."
        ) { navigate() }
    }

    fun navigate() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_navFragment_to_picmePreviewFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}