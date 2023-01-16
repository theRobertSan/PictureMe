package com.example.pictureme.views

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.MultiChoiceModeListener
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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

        // Dialog warning user to enable permissions
        val listener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(requireContext())
            .withTitle("Permissions Required")
            .withMessage("To take a PicMe, you have to enable those permissions.")
            .withButtonText("OK")
            .build()

        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_navFragment_to_picmePreviewFragment)
                    } else {
                        listener.onPermissionsChecked(report)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                    listener.onPermissionRationaleShouldBeShown(permissions, token)
                }
            }).check()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}