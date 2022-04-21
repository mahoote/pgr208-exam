package no.kristiania.prg208_1_exam

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.fragments.UploadImageFragment
import no.kristiania.prg208_1_exam.permissions.CameraPermission
import no.kristiania.prg208_1_exam.permissions.ReadExternalStorage

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        findViewById<AppCompatButton>(R.id.m_select_image_btn).setOnClickListener {
            val requestCode = Globals.GALLERY_REQUEST_CODE

            if (ReadExternalStorage.askForStoragePermissions(this, requestCode)) {
                startActivityForResult(
                    Globals.openImageGallery(),
                    requestCode
                )
            }
            else {
                Toast.makeText(applicationContext, "Unable to open gallery", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        findViewById<AppCompatButton>(R.id.m_take_photo_btn).setOnClickListener {
            val requestCode = Globals.CAMERA_REQUEST_CODE

            if (CameraPermission.askForStoragePermissions(this, requestCode)) {
                startActivityForResult(
                    Globals.openCamera(),
                    requestCode
                )
            } else {
                Toast.makeText(applicationContext, "Unable to open camera", Toast.LENGTH_SHORT)
                    .show()
            }
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
                Globals.GALLERY_REQUEST_CODE -> {
                    val imageUri = data?.data
                    val uploadImageFragment = UploadImageFragment()
                    replaceFragment(uploadImageFragment, imageUri)
                }
                Globals.CAMERA_REQUEST_CODE -> {
                    // TODO: Save image to phone and get Uri
                    val bitmapImage = data?.extras?.get("data") as Bitmap
                    val imageUri = Globals.bitmapToUri(this, bitmapImage)
                    val uploadImageFragment = UploadImageFragment()
                    replaceFragment(uploadImageFragment, imageUri)
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, uri: Uri?) {
        val bundle = Bundle()
        bundle.putParcelable("imageUri", uri)
        fragment.arguments = bundle

        fragmentManager.beginTransaction()
            .replace(R.id.m_content_fragment_container, fragment, "content_fragment").commit()
    }
}