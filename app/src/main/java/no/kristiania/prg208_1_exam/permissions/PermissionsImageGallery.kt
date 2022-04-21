package no.kristiania.prg208_1_exam.permissions

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

object PermissionsImageGallery {

    const val requestCode = 100

    fun openImageGallery(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        return intent
    }

    private fun isPermissionsAllowed(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun askForStoragePermissions(context: Context): Boolean {
        if (!isPermissionsAllowed(context)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showPermissionDeniedDialog(context)
            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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