package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import no.kristiania.prg208_1_exam.Globals
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.SearchActivity
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.services.ApiService
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UploadImageFragment : Fragment() {

    private lateinit var imageUri: Uri
    private lateinit var filePath: String

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

            filePath = Globals.getPathFromURI(activity, imageUri)!!

            Globals.cachedImages.forEach {
                Log.d("Response", "Map: ${it.key}: ${it.value}")
            }

            if(Globals.cachedImages[filePath] == null) {
                Log.d("Response", "API get")
                uploadImageToServer(imageUri)
            } else {
                Log.d("Response", "Cache get")
                Globals.cachedImages[filePath]?.let { cachedImages -> getCachedImages(cachedImages) }
            }
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
            val file = File(Globals.getPathFromURI(activity, imageUri)!!)

            Log.d("Response", "uploadImageToServer: file: $file")

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

        Globals.cachedImages[filePath] = CachedImages(imageUri, images, Calendar.getInstance().time)

        startSearchActivity(SearchActivity(), results)
    }

    private fun getCachedImages(cachedImages: CachedImages) {
        Log.d("Response", "Get cached images")
        cachedImages.created = Calendar.getInstance().time
        val results = cachedImages.images as ArrayList<ResultImage>
        startSearchActivity(SearchActivity(), results)
    }

    // Dummy method!!
    private fun startSearchActivity(activity: Activity, results: ArrayList<ResultImage>){
        val activityClassName = activity::class.java.name
        val currentActivity = Globals.getCurrentActivity(context)

        if(!currentActivity.equals(activityClassName)) {
            val bundle = Bundle()
            bundle.putString("chosenImageUri", imageUri.toString())
            bundle.putSerializable("results", results)

            val intent = Intent(this.requireContext(), activity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            Log.d("m_debug", "Starting activity!")
        }
    }
}