package no.kristiania.prg208_1_exam

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import no.kristiania.prg208_1_exam.fragments.HeaderFragment

object Globals : AppCompatActivity() {

    const val LOG_TAG = "debugger"

    fun setHeaderFragment(fragmentManager: FragmentManager){

        val headerFragment = HeaderFragment()

        // Add header fragment to activity
        fragmentManager.beginTransaction().add(R.id.header_fragment_container, headerFragment).commit()
    }

    //    TODO: Find alternative way.
    fun getCurrentActivity(context: Context?):String? {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningTasks(1)[0].topActivity?.className
    }

    fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
        val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
        return image
    }

    fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
        return decoder(context, id, uri)
    }
}