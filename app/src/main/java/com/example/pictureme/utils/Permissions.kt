package com.example.pictureme.utils

import android.content.Context
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

object Permissions {

    fun checkPermissions(
        context: Context,
        permissions: List<String>,
        message: String,
        toInvoke: () -> Unit,
    ) {
        // Dialog warning user to enable permissions
        val listener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(context)
            .withTitle("Permissions Required")
            .withMessage(message)
            .withButtonText("OK")
            .build()

        Dexter.withContext(context)
            .withPermissions(
                permissions
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        toInvoke.invoke()
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

}