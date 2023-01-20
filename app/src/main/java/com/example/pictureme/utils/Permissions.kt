package com.example.pictureme.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
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

        if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gps_enabled = false
            var network_enabled = false

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            } catch (ex: Exception) {
            }

            if (!gps_enabled && !network_enabled) {
//            // notify user
                AlertDialog.Builder(context)
                    .setMessage("You have to enable Location")
                    .setPositiveButton("OK",
                        DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                            context.startActivity(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            )
                        })
                    .setNegativeButton("Cancel", null)
                    .show()
                return
            }
        }


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