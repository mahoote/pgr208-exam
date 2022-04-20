package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.jacksonandroidnetworking.JacksonParserFactory
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.Globals
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.SearchActivity
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.services.ApiService
import java.io.File

class UploadImageFragment : Fragment() {

    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_upload_image, container, false)

        initializeAndroidNetworking()

        val uploadImage = v.findViewById<ImageView>(R.id.uf_upload_img)
        val imgTxtStatus = v.findViewById<TextView>(R.id.uf_upload_img_status_txt)

        imageUri = arguments?.getParcelable("imageUri")!!
        Globals.loadImage(imageUri.toString(), uploadImage, imgTxtStatus)
        uploadImage.maxWidth = 800
        uploadImage.maxHeight = 800

        v.findViewById<AppCompatButton>(R.id.uf_upload_search_btn).setOnClickListener{
            uploadImageToServer(imageUri)
        }

        return v
    }

    private fun initializeAndroidNetworking() {
        AndroidNetworking.initialize(activity?.applicationContext)
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    private fun uploadImageToServer(imageUri: Uri?) {
        Log.d("m_debug", "uploadImageToServer")
        if (imageUri != null) {
            val file = File(getPathFromURI(imageUri)!!)
            ApiService().postImage(this, file)
        }
    }

    fun onErrorResponse(anError: ANError) {
        Log.d("Response", "An error occured")
        anError.printStackTrace()
        //Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
    }

    fun onSuccessfulPost(response: String) {
        Log.d("Response", "Response = Success!")
        Log.d("Response", "After api: $response")

        ApiService().getImages(this, "bing", response)
    }

    fun onSuccessfulGet(images: List<ResultImage?>){
        Log.d("Response", "Get successful")
        val results = images as ArrayList<ResultImage>
        startSearchActivity(SearchActivity(), results)
    }

    private fun getPathFromURI(uri: Uri?): String? {
        val cursor: Cursor? = uri?.let {activity?.contentResolver?.query(it, null, null, null, null) }
        cursor?.moveToFirst()
        val idx: Int? = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return idx?.let { cursor?.getString(it) }
    }

    // Dummy method!!
    private fun startSearchActivity(activity: Activity, results: ArrayList<ResultImage>){
        val activityClassName = activity::class.java.name
        val currentActivity = Globals.getCurrentActivity(context)

        if(!currentActivity.equals(activityClassName)) {
            Log.d("m_debug", "Starting activity!")
            val intent = Intent(this.requireContext(), activity::class.java)
//            intent.putExtra("chosenImageUri", imageUri)
//            intent.putExtra("results", results)
            startActivity(intent)
        }
    }
}