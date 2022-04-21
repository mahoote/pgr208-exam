package no.kristiania.prg208_1_exam

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
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
import java.lang.Exception

object Globals : AppCompatActivity() {

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
                statusTxt.text = resources.getString(R.string.error_message)
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

    fun uriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
        val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
        return image
    }

    fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
        return decoder(context, id, uri)
    }

    fun getPathFromURI(activity: Activity?, uri: Uri): String? {
        val cursor: Cursor? = uri?.let {activity?.contentResolver?.query(it, null, null, null, null) }
        cursor?.moveToFirst()
        val idx: Int? = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return idx?.let { cursor.getString(it) }
    }
}