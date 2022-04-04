package no.kristiania.prg208_1_exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.fragments.HeaderFragment
import no.kristiania.prg208_1_exam.fragments.UploadImageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)

        val selectImage = findViewById<AppCompatButton>(R.id.m_select_image_btn)
        val uploadImageFragment = UploadImageFragment()

        selectImage.setOnClickListener {
            switchFragment(uploadImageFragment)
        }
    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.m_content_fragment_container, fragment, "content_fragment").commit()
    }

    override fun onBackPressed() {
        val contentFragment = fragmentManager.findFragmentByTag("content_fragment")

        if (contentFragment != null) {
            fragmentManager.beginTransaction().remove(contentFragment).commit()
        } else {
            finishAffinity()
        }
    }
}