package no.kristiania.prg208_1_exam.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.jacksonandroidnetworking.JacksonParserFactory
import com.theartofdev.edmodo.cropper.CropImageView
import no.kristiania.prg208_1_exam.*
import no.kristiania.prg208_1_exam.dialogs.LoadingDialog
import no.kristiania.prg208_1_exam.Globals
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.SearchActivity
import no.kristiania.prg208_1_exam.db.DataBaseHelper
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.permissions.ReadExternalStorage
import no.kristiania.prg208_1_exam.runnables.FetchImagesRunnable
import no.kristiania.prg208_1_exam.services.ApiService
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UploadImageFragment : Fragment() {

    val TAG: String = "m_debug"

    private lateinit var imageUri: Uri
    private lateinit var imageFilePath: String
    private var dbHelper: DataBaseHelper? = null
    private lateinit var loadingDialog: LoadingDialog
    private var waitForThread: Boolean = true

    private lateinit var googleThread: Thread
    private lateinit var bingThread: Thread
    private lateinit var tineyeThread: Thread

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_upload_image, container, false)

        initializeAndroidNetworking()

        val uploadImage = v.findViewById<CropImageView>(R.id.uf_upload_img)

        val origUri: Uri? = arguments?.getParcelable("imageUri")

        origUri?.let {
            imageUri = it

            Log.d(TAG, "onCreateView: Imageuri: $imageUri")

            printRealPath(imageUri)

            uploadImage.setImageUriAsync(imageUri)
            uploadImage.isShowCropOverlay = false
        }

        loadingDialog = LoadingDialog(requireActivity())

        // Upload btn.
        v.findViewById<ImageButton>(R.id.uf_upload_search_btn).setOnClickListener{
            if(uploadImage.isShowCropOverlay) {
                val cropped: Bitmap = uploadImage.croppedImage
                val fileName = Globals.getFileNameFromUri(requireContext(), imageUri)
                imageUri = context?.let { context -> Globals.bitmapToUri(context, cropped, "${fileName}_crop") }!!
            }
            retrieveImagesFromSrc()
        }

        // Select new image btn.
        v.findViewById<AppCompatImageButton>(R.id.uf_select_new_btn).setOnClickListener {
            val mainActivity = activity as MainActivity
            val requestCode = Globals.GALLERY_REQUEST_CODE

            if (ReadExternalStorage.askForStoragePermissions(mainActivity, requestCode)) {
                mainActivity.startActivityForResult(Globals.openImageGallery(), requestCode)
            }
        }
        // Take new photo btn.
        v.findViewById<AppCompatImageButton>(R.id.uf_take_photo_btn).setOnClickListener {
            val mainActivity = activity as MainActivity
            val requestCode = Globals.CAMERA_REQUEST_CODE

            if (ReadExternalStorage.askForStoragePermissions(mainActivity, requestCode)) {
                mainActivity.startActivityForResult(Globals.openCamera(requireContext()), requestCode)
            }
        }

        // Crop btn.
        val cropBtn = v.findViewById<AppCompatImageButton>(R.id.uf_crop_btn)

        cropBtn.setOnClickListener {
            val isChecked = !uploadImage.isShowCropOverlay
            uploadImage.isShowCropOverlay = isChecked

            if(!isChecked) {
                cropBtn.alpha = 1f
                uploadImage.resetCropRect()
            } else {
                cropBtn.alpha = 0.7f
            }
        }

        // Rotate switch.
        v.findViewById<AppCompatImageButton>(R.id.uf_rotate_btn).setOnClickListener {
            val degree = uploadImage.rotation
            val plusDegree = 90f

            uploadImage.rotation = degree + plusDegree
        }

        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbHelper = DataBaseHelper(context)
    }

    private fun printRealPath(uri: Uri, TAG: String = "m_debug") {
        val path = Globals.getPathFromURI(requireActivity(), uri)
        Log.d(TAG, "printRealPath: $path")
    }

    private fun retrieveImagesFromSrc() {
        waitForThread = true
        loadingDialog.startLoadingDialog()
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

        if(waitForThread) {
            waitForThread = false
            val errorStatusTxt = requireActivity().findViewById<TextView>(R.id.uf_error_status_txt)
            errorStatusTxt.visibility = VISIBLE
            loadingDialog.endLoadingDialog()
        }
    }

    fun onSuccessfulPost(response: String) {
        Log.d("Response", "Response = Success!")
        Log.d("Response", "After api: $response")

        dbHelper?.putOriginalImage(DBOriginalImage(null, Uri.parse(response), Calendar.getInstance().time.toString()))

        val fetchBingRunnable = FetchImagesRunnable(this,"bing", response)
        val fetchGoogleRunnable = FetchImagesRunnable(this,"google", response)
        val fetchTineyeRunnable = FetchImagesRunnable(this,"tineye", response)

        googleThread = Thread(fetchGoogleRunnable)
        bingThread = Thread(fetchBingRunnable)
        tineyeThread = Thread(fetchTineyeRunnable)

        googleThread.start()
        bingThread.start()
        tineyeThread.start()
    }

    fun onSuccessfulGet(images: ArrayList<ResultImage?>, returnEngine: String, url: String) {
        Log.d("r_debug", "onSuccessfulGet: Wait for thread: $waitForThread")
        if(waitForThread) {
            waitForThread = false
            Log.d("Response", "Get successful")
            Log.d("r_debug", "onSuccessfulGet: Return images from $returnEngine")
            Globals.cachedImages[imageFilePath] = CachedImages(imageUri, images, Calendar.getInstance().time)

            val originalImageID = dbHelper?.getOriginalImageByUri(url)?.id
            val dbResultImages = originalImageID?.let { Globals.convertResultImagesToDBModel(images, it) }

            if (dbResultImages != null) {
                dbHelper?.putResultImages(dbResultImages)
            }

            startSearchActivity(SearchActivity(), images)
        }

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
            loadingDialog.endLoadingDialog()
        }
    }
}