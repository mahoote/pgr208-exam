package no.kristiania.prg208_1_exam

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.fragments.HeaderNavFragment
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object Globals : AppCompatActivity() {

    const val GALLERY_REQUEST_CODE = 1
    const val CAMERA_REQUEST_CODE = 2

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

    fun setHeaderFragment(fragmentManager: FragmentManager){

        val headerFragment = HeaderNavFragment()

        // Add header fragment to activity
        fragmentManager.beginTransaction().add(R.id.header_fragment_container, headerFragment).commit()
    }

    //    TODO: Find alternative way.
    fun getCurrentActivity(context: Context?):String? {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningTasks(1)[0].topActivity?.className
    }

    fun uriToBitmap(context: Context, uri: String): Bitmap {
        val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
        return image
    }

    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "JPEG_${timeStamp}.jpeg", null)
        return Uri.parse(path)
    }

    fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
        return decoder(context, id, uri)
    }

    fun getPathFromURI(activity: Activity?, uri: Uri): String? {
        val cursor: Cursor? = uri.let {activity?.contentResolver?.query(it, null, null, null, null) }
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

    fun openCamera(): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }
}