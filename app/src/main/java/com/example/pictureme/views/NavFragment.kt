package com.example.pictureme.views

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentNavBinding

class NavFragment : Fragment() {

    val REQUEST_CODE = 200

    private var _binding: FragmentNavBinding? = null
    private val binding get() = _binding!!

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
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = binding.bottomNavView
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        setupWithNavController(bottomNavigationView, navController)
    }

    private fun setupCameraButton() {

        val requestPermissionLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val permissionsRejected: MutableList<String> = ArrayList()
            for (permission in permissions) {
                if (permission.value) {
                    println("REJECTED: " + permission.key)
                    permissionsRejected.add(permission.key)
                }
            }

            Navigation.findNavController(binding.root).navigate(R.id.action_navFragment_to_picmePreviewFragment)

        }

        // Required Permissions
        val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        binding.buttonCamera.setOnClickListener {

            // Check if permission has already been granted
            val permissionRequests: MutableList<String> = ArrayList()
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(activity as Activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionRequests.add(permission)
                }
            }

            if (permissionRequests.isNotEmpty()) {
                requestPermissionLauncher.launch(permissions.toTypedArray())
//                ActivityCompat.requestPermissions(
//                    activity as Activity,
//                    permissionRequests.toTypedArray(),
//                    REQUEST_CODE)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}