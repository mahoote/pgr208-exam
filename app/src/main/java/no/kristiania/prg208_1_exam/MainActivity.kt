package no.kristiania.prg208_1_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.kristiania.prg208_1_exam.fragments.HeaderFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val headerFragment = HeaderFragment()

        // Add header fragment to activity
        fragmentManager.beginTransaction().add(R.id.header_fragment_container, headerFragment).commit()
    }
}