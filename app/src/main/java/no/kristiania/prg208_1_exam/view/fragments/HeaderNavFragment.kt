package no.kristiania.prg208_1_exam.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import no.kristiania.prg208_1_exam.*
import no.kristiania.prg208_1_exam.controller.service.DatabaseService
import no.kristiania.prg208_1_exam.controller.utils.Utils
import no.kristiania.prg208_1_exam.view.MainActivity
import no.kristiania.prg208_1_exam.view.SavedActivity
import no.kristiania.prg208_1_exam.view.SearchActivity

class HeaderNavFragment : Fragment() {

    private lateinit var v: View
    private lateinit var dbService: DatabaseService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_nav_header, container, false)

        dbService = DatabaseService(requireContext())

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
        val currentActivity = Utils.getCurrentActivity(context)

        if(!currentActivity.equals(activityClassName)) {
            val intent = Intent(requireContext(), activity::class.java)
            startActivity(intent)
        }
    }

    private fun highlightCurrentActivity() {
        val currentActivity = Utils.getCurrentActivity(context)
        if(currentActivity != null) {
            when {
                currentActivity.contains("MainActivity") -> {
                    setBtnDetails(v.findViewById<AppCompatButton>(R.id.hf_new_image_btn))
                }
                currentActivity.contains("SearchActivity") -> {
                    setBtnDetails(v.findViewById<AppCompatButton>(R.id.hf_search_btn))
                }
                currentActivity.contains("SavedActivity") -> {
                    setBtnDetails(v.findViewById<AppCompatButton>(R.id.hf_saved_btn))
                }
            }
        }
    }

    private fun setBtnDetails(btn: AppCompatButton) {
        btn.background = ContextCompat.getDrawable(requireContext(), R.drawable.nav_blue_btn)
        btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_blue_text_color))
    }

}