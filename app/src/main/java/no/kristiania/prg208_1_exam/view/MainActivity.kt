package no.kristiania.prg208_1_exam.view

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.controller.utils.Utils
import no.kristiania.prg208_1_exam.view.fragments.UploadImageFragment
import no.kristiania.prg208_1_exam.controller.permissions.CameraPermission
import no.kristiania.prg208_1_exam.controller.permissions.ReadExternalStorage


class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager

        Utils.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        // Select image onclick
        findViewById<AppCompatButton>(R.id.m_select_image_btn).setOnClickListener {
            openGallery()
        }
        // Take photo onclick
        findViewById<AppCompatButton>(R.id.m_take_photo_btn).setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        val requestCode = Utils.CAMERA_REQUEST_CODE

        if (CameraPermission.askForStoragePermissions(this, requestCode)) {
            startActivityForResult(
                Utils.openCamera(this),
                requestCode
            )
        } else {
            Toast.makeText(applicationContext, "Give permission to open camera", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openGallery() {
        val requestCode = Utils.GALLERY_REQUEST_CODE

        if (ReadExternalStorage.askForStoragePermissions(this, requestCode)) {
            startActivityForResult(
                Utils.openImageGallery(),
                requestCode
            )
        } else {
            Toast.makeText(
                applicationContext,
                "Give permission to open gallery",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onBackPressed() {
        val contentFragment = fragmentManager.findFragmentByTag("content_fragment")

        if (contentFragment != null) {
            fragmentManager.beginTransaction().remove(contentFragment).commit()
        } else {
            finishAffinity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Utils.GALLERY_REQUEST_CODE -> {
                    val imageUri = data?.data?.let { Utils.uriToJPEG(this, it) }
                    val uploadImageFragment = UploadImageFragment()
                    replaceFragment(uploadImageFragment, imageUri)
                }
                Utils.CAMERA_REQUEST_CODE -> {
                    val currentPhotoPath: String = Utils.currentPhotoPath

                    Log.d("m_debug", "openCamera: imagepath: $currentPhotoPath")

                    val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    val fileName = Utils.getFileNameFromPath(currentPhotoPath)
                    val imageUri = Utils.bitmapToUri(this, bitmap, fileName)

                    val uploadImageFragment = UploadImageFragment()
                    replaceFragment(uploadImageFragment, imageUri)
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, uri: Uri?) {
        val bundle = Bundle()
        // TODO: Parcelable implementation reference mainactivity to uploadFragment
        bundle.putParcelable("imageUri", uri)
        fragment.arguments = bundle

        fragmentManager.beginTransaction()
            .replace(R.id.m_content_fragment_container, fragment, "content_fragment").commit()
    }
}