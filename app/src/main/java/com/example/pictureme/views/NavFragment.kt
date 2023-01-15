package com.example.pictureme.views

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentNavBinding
import com.example.pictureme.utils.Permissions
import com.example.pictureme.viewmodels.PicmeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NavFragment : Fragment() {

    private var _binding: FragmentNavBinding? = null
    private val binding get() = _binding!!

    private val picmeViewModel by activityViewModels<PicmeViewModel>()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        println("Hi")
//        picmeViewModel.loadPicmes()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavBinding.inflate(inflater, container, false);

        setupBottomNav()
        setupCameraButton()

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

        val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                println("2")
                println(permissions)

                val permissionsRejected: MutableList<String> = ArrayList()
                for (permission in permissions) {
                    if (!permission.value) {
                        println("REJECTED: " + permission.key)
                        permissionsRejected.add(permission.key)
                    }
                }

                // If all permissions granted, go to camera
                if (permissionsRejected.isEmpty()) {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_navFragment_to_picmePreviewFragment)
                }

            }

        // Required Permissions
        val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        binding.buttonCamera.setOnClickListener {

            // Check if permission has already been granted
            val permissionRequests: MutableList<String> = ArrayList()
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        activity as Activity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionRequests.add(permission)
                }
            }

            println("1")
            println(permissionRequests)

            if (permissionRequests.isNotEmpty()) {
                requestPermissionLauncher.launch(permissions.toTypedArray())
            } else {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_navFragment_to_picmePreviewFragment)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}