package no.kristiania.prg208_1_exam.services

import android.app.Activity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import no.kristiania.prg208_1_exam.SearchActivity
import java.io.File


class APIService {

    fun postImage(activity: SearchActivity, file: File) {

        AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                // do anything with progress
            }

            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    activity.onSuccessfulResponse(response)
                }

                override fun onError(anError: ANError) {
                    activity.onErrorResponse(ANError())
                }

            })
    }
}