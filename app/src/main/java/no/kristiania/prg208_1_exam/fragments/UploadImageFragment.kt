package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.jacksonandroidnetworking.JacksonParserFactory
import no.kristiania.prg208_1_exam.*
import no.kristiania.prg208_1_exam.dialogs.LoadingDialog
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.permissions.PermissionsImageGallery
import no.kristiania.prg208_1_exam.services.ApiService
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UploadImageFragment : Fragment() {

    private lateinit var imageUri: Uri
    private lateinit var imageFilePath: String
    private lateinit var loadingDialog: LoadingDialog

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

        loadingDialog = LoadingDialog(requireActivity())

        v.findViewById<AppCompatButton>(R.id.uf_upload_search_btn).setOnClickListener{
            loadingDialog.startLoadingDialog()
            retrieveImagesFromSrc()
        }

        v.findViewById<AppCompatButton>(R.id.uf_select_new_btn).setOnClickListener {
            val mainActivity = activity as MainActivity

            if (PermissionsImageGallery.askForStoragePermissions(mainActivity)) {
                mainActivity.startActivityForResult(PermissionsImageGallery.openImageGallery(), PermissionsImageGallery.requestCode)
            }
        }
        return v
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
        Log.e("Response", "An error occurred: ${Log.getStackTraceString(anError)}")

        val errorStatusTxt = activity?.findViewById<TextView>(R.id.uf_error_status_txt)
        errorStatusTxt?.visibility = VISIBLE
        loadingDialog.dismissDialog()
    }

    fun onSuccessfulPost(response: String) {
        Log.d("Response", "Response = Success!")
        Log.d("Response", "After api: $response")

        ApiService().getImages(this, "bing", response)
    }

    fun onSuccessfulGet(images: ArrayList<ResultImage?>){
        Log.d("Response", "Get successful")
        Globals.cachedImages[imageFilePath] = CachedImages(imageUri, images, Calendar.getInstance().time)
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