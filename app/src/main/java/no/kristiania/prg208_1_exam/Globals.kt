package no.kristiania.prg208_1_exam

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.fragments.HeaderFragment

object Globals : AppCompatActivity() {

    fun setHeaderFragment(fragmentManager: FragmentManager){

        val headerFragment = HeaderFragment()

        // Add header fragment to activity
        fragmentManager.beginTransaction().add(R.id.header_fragment_container, headerFragment).commit()
    }
}