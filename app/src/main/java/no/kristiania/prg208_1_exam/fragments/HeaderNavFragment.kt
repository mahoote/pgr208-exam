package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import no.kristiania.prg208_1_exam.*

class HeaderNavFragment : Fragment() {

    private lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nav_header, container, false)

        highlightCurrentActivity()
        headerButtonsOnClick()

        return v
    }

    private fun headerButtonsOnClick() {
        v.findViewById<AppCompatButton>(R.id.hf_new_image_btn).setOnClickListener { newImageOnClick() }

        v.findViewById<AppCompatButton>(R.id.hf_search_btn).setOnClickListener {
            startEmptyActivity(SearchActivity())
        }

        v.findViewById<AppCompatButton>(R.id.hf_saved_btn).setOnClickListener {
           startEmptyActivity(SavedActivity())
        }
    }

    private fun newImageOnClick() {
        val fragmentManager = parentFragmentManager

        // TODO: Refactor code. Similar with MainActivity onBackPressed().
        val contentFragment = fragmentManager.findFragmentByTag("content_fragment")

        if (contentFragment != null) {
            fragmentManager.beginTransaction().remove(contentFragment).commit()
        } else {
            startEmptyActivity(MainActivity())
        }
    }

    private fun startEmptyActivity(activity: Activity){
        val activityClassName = activity::class.java.name
        val currentActivity = Globals.getCurrentActivity(context)

        if(!currentActivity.equals(activityClassName)) {
            val intent = Intent(requireContext(), activity::class.java)
            startActivity(intent)
        }
    }

    private fun highlightCurrentActivity() {
        val currentActivity = Globals.getCurrentActivity(context)
        if(currentActivity != null) {
            val blueBtn: Drawable? = context?.let { ContextCompat.getDrawable(it, R.drawable.nav_blue_btn) }
            when {
                currentActivity.contains("MainActivity") -> {
                    v.findViewById<AppCompatButton>(R.id.hf_new_image_btn).background = blueBtn
                }
                currentActivity.contains("SearchActivity") -> {
                    v.findViewById<AppCompatButton>(R.id.hf_search_btn).background = blueBtn
                }
                currentActivity.contains("SavedActivity") -> {
                    v.findViewById<AppCompatButton>(R.id.hf_saved_btn).background = blueBtn
                }
            }
        }
    }

}