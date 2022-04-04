package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import no.kristiania.prg208_1_exam.MainActivity
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.SearchActivity

class HeaderFragment : Fragment() {

    private lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_header, container, false)

        highlightCurrentActivity()
        newImageOnClick()

        v.findViewById<AppCompatButton>(R.id.hf_search_btn).setOnClickListener {
            startEmptyActivity(SearchActivity())
        }

        return v
    }

    private fun newImageOnClick() {
        v.findViewById<AppCompatButton>(R.id.hf_new_image_btn).setOnClickListener {
            Toast.makeText(context, "Click new image", Toast.LENGTH_SHORT).show()
            val fragmentManager = parentFragmentManager

            // TODO: Refactor code. Similar with MainActivity onBackPressed().
            val contentFragment = fragmentManager.findFragmentByTag("content_fragment")

            if (contentFragment != null) {
                fragmentManager.beginTransaction().remove(contentFragment).commit()
            } else {
                startEmptyActivity(MainActivity())
            }
        }
    }

    private fun startEmptyActivity(activity: Activity){
        val intent = Intent(this.requireContext(), activity::class.java)
        v.clearAnimation()
        startActivity(intent)
    }

    private fun highlightCurrentActivity() {
        val currentActivity = getCurrentActivity()
        if(currentActivity != null) {
            val blueBtn: Drawable? = context?.let { ContextCompat.getDrawable(it, R.drawable.nav_blue_btn) }
            when {
                currentActivity.contains("MainActivity") -> {
                    v.findViewById<AppCompatButton>(R.id.hf_new_image_btn).background = blueBtn
                }
                currentActivity.contains("SearchActivity") -> {
                    v.findViewById<AppCompatButton>(R.id.hf_search_btn).background = blueBtn
                }
            }
        }
    }

//    TODO: Find alternative way.
    private fun getCurrentActivity():String? {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningTasks(1)[0].topActivity?.className
    }

}