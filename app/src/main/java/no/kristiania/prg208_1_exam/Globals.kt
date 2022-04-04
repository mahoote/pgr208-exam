package no.kristiania.prg208_1_exam

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.fragments.HeaderFragment

object Globals : AppCompatActivity() {

    fun setHeaderFragment(fragmentManager: FragmentManager){

        val headerFragment = HeaderFragment()

        // Add header fragment to activity
        fragmentManager.beginTransaction().add(R.id.header_fragment_container, headerFragment).commit()
    }

    //    TODO: Find alternative way.
    fun getCurrentActivity(context: Context?):String? {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningTasks(1)[0].topActivity?.className
    }
}