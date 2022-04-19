package no.kristiania.prg208_1_exam.services

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import org.json.JSONObject
import java.io.File
import java.util.concurrent.Executors


class APIService {

    fun postImage(file: File){
        AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                // do anything with progress
            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    Log.d("debug", response.toString())
                }

                override fun onError(anError: ANError?) {
                    anError?.printStackTrace()
                }

            })
    }

    fun postImagse(){

    }
}