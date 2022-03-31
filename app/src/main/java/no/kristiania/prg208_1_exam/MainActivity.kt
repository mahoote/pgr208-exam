package no.kristiania.prg208_1_exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.fragments.HeaderFragment
import no.kristiania.prg208_1_exam.fragments.UploadImageFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val headerFragment = HeaderFragment()

        // Add header fragment to activity
        fragmentManager.beginTransaction().add(R.id.m_header_fragment_container, headerFragment).commit()

        val selectImage = findViewById<AppCompatButton>(R.id.m_select_image_btn)
        val uploadImageFragment = UploadImageFragment()

        selectImage.setOnClickListener {
            switchFragment(uploadImageFragment)
        }
    }

    private fun switchFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.m_content_fragment_container, fragment, "content_fragment").commit()
    }
}