package no.kristiania.prg208_1_exam

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.fragments.UploadImageFragment
import no.kristiania.prg208_1_exam.permissions.PermissionsImageGallery

class MainActivity : AppCompatActivity() {

    private val requestCode = 100
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        val selectImage = findViewById<AppCompatButton>(R.id.m_select_image_btn)

        selectImage.setOnClickListener {
            if (PermissionsImageGallery.askForStoragePermissions(this)) {
                startActivityForResult(PermissionsImageGallery.openImageGallery(), PermissionsImageGallery.requestCode)
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
        if (resultCode == Activity.RESULT_OK && requestCode == this.requestCode){
            val imageUri = data?.data

            val uploadImageFragment = UploadImageFragment()
            replaceFragment(uploadImageFragment, imageUri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            this.requestCode -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()
                }
                return
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