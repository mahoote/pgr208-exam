package no.kristiania.prg208_1_exam

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.fragments.HeaderNavFragment
import no.kristiania.prg208_1_exam.models.DBResultImage
import no.kristiania.prg208_1_exam.models.ResultImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.widget.FrameLayout
import androidx.core.content.ContentProviderCompat.requireContext
import no.kristiania.prg208_1_exam.fragments.NoSavedResultsFragment


object Globals : AppCompatActivity() {

    const val GALLERY_REQUEST_CODE = 100
    const val CAMERA_REQUEST_CODE = 101
    lateinit var currentPhotoPath: String

    val cachedImages: MutableMap<String, CachedImages> = mutableMapOf()

    fun loadImage(
        uriString: String,
        target: ImageView?,
        statusTxt: TextView
    ) {
        Picasso.get().load(uriString).into(target, object : Callback {
            override fun onSuccess() {
                statusTxt.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                statusTxt.text = resources.getString(R.string.error_message_01)
                Log.e("Error", "Picasso load image: ${e?.printStackTrace()}")
                e?.printStackTrace()
            }
        })
    }

    fun setHeaderFragment(fragmentManager: FragmentManager) {
        val headerFragment = HeaderNavFragment()
        // Add header fragment to activity
        fragmentManager.beginTransaction().add(R.id.header_fragment_container, headerFragment)
            .commit()
    }

    //    TODO: Find alternative way.
    fun getCurrentActivity(context: Context?): String? {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningTasks(1)[0].topActivity?.className
    }

    fun uriToBitmap(context: Context, uri: String): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(uri))
    }

    fun bitmapToUri(context: Context, bitmap: Bitmap, fileName: String): Uri {
        Log.i("MethodAction", "Saving image to phone...")
        val compressedBitmap = compressBitmap(bitmap)

//        If we want timestamp in filename.
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val path =
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                compressedBitmap,
                "$fileName.jpeg",
                null
            )
        return Uri.parse(path)
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        return createScaledBitmap(bitmap, 800)
    }

    private fun createScaledBitmap(bitmap: Bitmap, maxSize: Int): Bitmap? {
        val bWidth = bitmap.width
        val bHeight = bitmap.height

        Log.d("m_debug", "createScaledBitmap: Original scale = width: $bWidth, height: $bHeight")

        val compressedBitmap = when {
            bWidth > bHeight -> {
                if (bWidth < maxSize) {
                    val diff = maxSize.toFloat() / bWidth
                    val scaledHeight = bHeight * diff
                    Bitmap.createScaledBitmap(bitmap, maxSize, scaledHeight.toInt(), true)
                } else {
                    val diff = bWidth.toFloat() / maxSize
                    val scaledHeight = bHeight / diff
                    Bitmap.createScaledBitmap(bitmap, maxSize, scaledHeight.toInt(), true)
                }
            }
            bHeight > bWidth -> {
                if (bHeight < maxSize) {
                    val diff = maxSize.toFloat() / bHeight
                    val scaledWidth = bWidth * diff
                    Bitmap.createScaledBitmap(bitmap, scaledWidth.toInt(), maxSize, true)
                } else {
                    val diff = bHeight.toFloat() / maxSize
                    val scaledWidth = bWidth / diff
                    Bitmap.createScaledBitmap(bitmap, scaledWidth.toInt(), maxSize, true)
                }
            }
            else -> {
                Bitmap.createScaledBitmap(bitmap, maxSize, maxSize, true)
            }
        }
        return compressedBitmap
    }

    fun getBitmap(
        context: Context,
        id: Int?,
        uri: String?,
        decoder: (Context, Int?, String?) -> Bitmap
    ): Bitmap {
        return decoder(context, id, uri)
    }

    fun getPathFromURI(activity: Activity?, uri: Uri): String? {
        val cursor: Cursor? =
            uri.let { activity?.contentResolver?.query(it, null, null, null, null) }
        cursor?.moveToFirst()
        val idx: Int? = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val path = idx?.let { cursor.getString(it) }
        cursor?.close()
        return path
    }

    fun openImageGallery(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        return intent
    }


    fun openCamera(context: Context): Intent {
        val fileName = "photo"
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(fileName, ".jpg", storageDirectory)

        currentPhotoPath = imageFile.absolutePath

        Log.d("m_debug", "openCamera: imagepath: $currentPhotoPath")

        val imageUri = FileProvider.getUriForFile(context, "no.kristiania.prg208_1_exam.fileprovider", imageFile)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        return intent
    }

    fun uriToJPEG(context: Context, origUri: Uri): Uri {
        val fileName = getFileNameFromUri(context, origUri)

        Log.d("m_debug", "uriToJPEG: fileName: $fileName")

        val imageBitmap = uriToBitmap(context, origUri.toString())
        return bitmapToUri(context, imageBitmap, fileName)
    }

    fun getFileNameFromUri(context: Context, origUri: Uri): String {
        val fullFileName = getFileNameWithFormatFromUri(context, origUri)
        return removeFileFormat(fullFileName)
    }

    fun convertResultImagesToDBModel(resultImages: ArrayList<ResultImage?>, originalImageId: Int) : ArrayList<DBResultImage> {
        val dbResultImages = arrayListOf<DBResultImage>()

        resultImages.forEach { resImg ->
            val dbResultImage = DBResultImage(
                null,
                resImg?.store_link,
                resImg?.name,
                resImg?.domain,
                resImg?.tracking_id,
                resImg?.thumbnail_link,
                resImg?.thumbnail_link,
                resImg?.description,
                resImg?.image_link,
                resImg?.current_date,
                originalImageId
            )
            dbResultImages.add(dbResultImage)
        }
        return dbResultImages
    }

    fun getFileNameFromPath(path: String): String {
        val fullFileName: String = path.substring(path.lastIndexOf("/") + 1)
        return removeFileFormat(fullFileName)
    }

    private fun removeFileFormat(fullFileName: String): String {
        Log.d("m_debug", "getFileNameWithoutFormat: full name: $fullFileName")

        val dotPos = fullFileName.indexOf(".")
        Log.d("m_debug", "getFileNameWithoutFormat: dot: $dotPos")

        return fullFileName.substring(0, dotPos)
    }

    @SuppressLint("Range")
    private fun getFileNameWithFormatFromUri(context: Context, uri: Uri): String {

        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (it != null) {
                    if(it.moveToFirst()) {
                        return it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }

        return uri.path?.lastIndexOf('/')?.plus(1)?.let { uri.path?.substring(it) }.toString()
    }

    fun Float.toDp(context: Context): Int {
        val metrics = context.resources.displayMetrics
        val fpixels = metrics.density * this
        return (fpixels + 0.5f).toInt()
    }

    fun showEmptyView(frameLayout: FrameLayout, fragmentContainer: Int, fragmentManager: FragmentManager) {
        frameLayout.visibility = View.VISIBLE

        fragmentManager.beginTransaction()
            .add(fragmentContainer, NoSavedResultsFragment(), "content-fragment")
            .commit()
    }

    fun toUrl(context: Context, uriString: String) {
        val uri = Uri.parse(uriString)
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    fun drawableToBitmap(drawable: BitmapDrawable): Bitmap {
        return drawable.bitmap
    }

    fun picassoLoad(src: String, imageView: ImageView) {
        Picasso.get().load(src).placeholder(R.drawable.result_image_placeholder).into(imageView)
    }

}