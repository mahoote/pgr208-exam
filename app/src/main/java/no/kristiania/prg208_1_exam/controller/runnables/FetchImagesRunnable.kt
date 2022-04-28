package no.kristiania.prg208_1_exam.controller.runnables

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import no.kristiania.prg208_1_exam.view.fragments.UploadImageFragment
import no.kristiania.prg208_1_exam.model.models.ResultImage
import no.kristiania.prg208_1_exam.controller.services.ApiService

class FetchImagesRunnable(private val fragment: UploadImageFragment, private val searchEngine: String, private val response: String) : Runnable {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        ApiService().getImages(this, fragment, searchEngine, response)
    }

    fun onSuccessfulGet(images: ArrayList<ResultImage?>, engine: String) {
        val mainLooper = Looper.getMainLooper()

        Handler(mainLooper).post {
            if(images.size > 0) {
                fragment.onSuccessfulGet(images, engine)
            }
        }
    }

}