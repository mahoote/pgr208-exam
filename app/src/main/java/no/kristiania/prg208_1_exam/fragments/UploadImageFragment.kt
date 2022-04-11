package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import no.kristiania.prg208_1_exam.Globals
import no.kristiania.prg208_1_exam.Globals.LOG_TAG
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.SearchActivity

class UploadImageFragment : Fragment() {

    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_upload_image, container, false)

        val uploadImage = v.findViewById<ImageView>(R.id.uf_upload_img)
        imageUri = arguments?.getParcelable("imageUri")!!
        uploadImage.setImageURI(imageUri)
        uploadImage.maxWidth = 800
        uploadImage.maxHeight = 800

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
            intent.putExtra("chosenImageUri", imageUri)
            startActivity(intent)
        }
    }
}