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
import androidx.appcompat.widget.AppCompatButton
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.jacksonandroidnetworking.JacksonParserFactory
import no.kristiania.prg208_1_exam.*
import no.kristiania.prg208_1_exam.dialogs.LoadingDialog
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.permissions.ReadExternalStorage
import no.kristiania.prg208_1_exam.services.ApiService
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UploadImageFragment : Fragment() {

    val TAG: String = "m_debug"

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

        // Upload btn.
        v.findViewById<AppCompatButton>(R.id.uf_upload_search_btn).setOnClickListener{
            loadingDialog.startLoadingDialog()
            retrieveImagesFromSrc()
        }

        // Select new image btn.
        v.findViewById<AppCompatButton>(R.id.uf_select_new_btn).setOnClickListener {
            val mainActivity = activity as MainActivity
            val requestCode = Globals.GALLERY_REQUEST_CODE

            if (ReadExternalStorage.askForStoragePermissions(mainActivity, requestCode)) {
                mainActivity.startActivityForResult(Globals.openImageGallery(), requestCode)
            }
        }
        return v
    }

    private fun printRealPath(uri: Uri, TAG: String = "m_debug") {
        val path = Globals.getPathFromURI(requireActivity(), uri)
        Log.d(TAG, "printRealPath: $path")
    }

    private fun retrieveImagesFromSrc() {
        imageFilePath = Globals.getPathFromURI(requireActivity(), imageUri).toString()

        if (Globals.cachedImages[imageFilePath] != null) {
            getCachedImages(Globals.cachedImages[imageFilePath])
        } else {
            uploadImageToServer(imageUri)
        }
    }

    private fun initializeAndroidNetworking() {
        AndroidNetworking.initialize(requireContext())
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    private fun uploadImageToServer(uri: Uri) {
        Log.d("m_debug", "uploadImageToServer")
        val uriPath = Globals.getPathFromURI(requireActivity(), uri).toString()
        val file = File(uriPath)
        Log.d(TAG, "uploadImageToServer: file: $file")
        ApiService().postImage(this, file)
    }

    fun onErrorResponse(anError: ANError) {
        Log.e("Response", "An error occurred: ${Log.getStackTraceString(anError)}")
        anError.printStackTrace()

        val errorStatusTxt = requireActivity().findViewById<TextView>(R.id.uf_error_status_txt)
        errorStatusTxt.visibility = VISIBLE
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
            loadingDialog.dismissDialog()
        }
    }
}