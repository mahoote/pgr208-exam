package no.kristiania.prg208_1_exam.runnables

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import no.kristiania.prg208_1_exam.fragments.UploadImageFragment
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.services.ApiService

class FetchImagesRunnable(private val fragment: UploadImageFragment, private val searchEngine: String, private val response: String) : Runnable {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        ApiService().getImages(this, fragment, searchEngine, response)
    }

    fun onSuccessfulGet(images: ArrayList<ResultImage?>, engine: String, url: String) {
        if(images.size > 0) {
            fragment.onSuccessfulGet(images, engine, url)
        }
    }

}