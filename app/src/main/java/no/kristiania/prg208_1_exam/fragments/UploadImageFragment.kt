package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import no.kristiania.prg208_1_exam.Globals
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.SearchActivity

class UploadImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_upload_image, container, false)

        v.findViewById<AppCompatButton>(R.id.uf_upload_search_btn).setOnClickListener{
            startEmptyActivity(SearchActivity())
        }

        return v
    }

    // Dummy method!!
    private fun startEmptyActivity(activity: Activity){
        val activityClassName = activity::class.java.name
        val currentActivity = Globals.getCurrentActivity(context)

        if(!currentActivity.equals(activityClassName)) {
            val intent = Intent(this.requireContext(), activity::class.java)
            startActivity(intent)
        }
    }
}