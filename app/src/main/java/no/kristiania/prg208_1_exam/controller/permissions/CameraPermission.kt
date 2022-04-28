package no.kristiania.prg208_1_exam.controller.permissions

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CameraPermission {

    private const val permission = Manifest.permission.CAMERA

    private fun isPermissionsAllowed(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun askForStoragePermissions(context: Context, requestCode: Int): Boolean {
        if (!isPermissionsAllowed(context)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    permission
                )
            ) {
                showPermissionDeniedDialog(context)
            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(permission),
                    requestCode
                )
            }
            return false
        }
        return true
    }

    private fun showPermissionDeniedDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                })
            .setNegativeButton("Cancel", null)
            .show()
    }

}