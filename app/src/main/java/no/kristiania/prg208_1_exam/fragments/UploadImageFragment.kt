package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Context
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
import no.kristiania.prg208_1_exam.db.DataBaseHelper
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.services.ApiService
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UploadImageFragment : Fragment() {

    private lateinit var imageUri: Uri
    private lateinit var imageFilePath: String
    private var dbHelper: DataBaseHelper? = null

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
            retrieveImagesFromSrc()
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHelper = DataBaseHelper(context)
    }

    private fun retrieveImagesFromSrc() {
        imageFilePath = Globals.getPathFromURI(activity, imageUri)!!

        if (Globals.cachedImages[imageFilePath] != null) {
            getCachedImages(Globals.cachedImages[imageFilePath])
        } else {
            uploadImageToServer(imageUri)
        }
    }

    private fun initializeAndroidNetworking() {
        AndroidNetworking.initialize(activity?.applicationContext)
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    private fun uploadImageToServer(imageUri: Uri?) {
        Log.d("m_debug", "uploadImageToServer")
        imageUri?.let { uri ->
            val pathFromUri = Globals.getPathFromURI(activity, uri)

            pathFromUri?.let { path ->
                val file = File(path)
                ApiService().postImage(this, file)
            }
        }
    }

    fun onErrorResponse(anError: ANError) {
        Log.d("Response", "An error occurred")
        anError.printStackTrace()
        //Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
    }

    fun onSuccessfulPost(response: String) {
        Log.d("Response", "Response = Success!")
        Log.d("Response", "After api: $response")

        ApiService().getImages(this, "bing", response)
        dbHelper?.putOriginalImage(DBOriginalImage(null, Uri.parse(response), Calendar.getInstance().time.toString()))
        Log.d("db", "uri when saving: " + response)
    }

    fun onSuccessfulGet(images: ArrayList<ResultImage?>, url: String){
        Log.d("Response", "Get successful")
        Globals.cachedImages[imageFilePath] = CachedImages(imageUri, images, Calendar.getInstance().time)

        val originalImageID = dbHelper?.getOriginalImageByUri(url)?.id
        val dbResultImages = originalImageID?.let { Globals.convertResultImagesToDBModel(images, it) }

        if (dbResultImages != null) {
            dbHelper?.putResultImages(dbResultImages)
        }
        startSearchActivity(SearchActivity(), images)
    }

    private fun getCachedImages(cachedImages: CachedImages?) {
        Log.d("Response", "Get cached images")
        cachedImages?.created = Calendar.getInstance().time
        val results = cachedImages?.images
        startSearchActivity(SearchActivity(), results)
    }

    private fun startSearchActivity(activity: Activity, results: ArrayList<ResultImage?>?){
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