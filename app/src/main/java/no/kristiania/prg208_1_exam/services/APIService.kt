package no.kristiania.prg208_1_exam.services

import android.net.Uri
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.androidnetworking.interfaces.StringRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import no.kristiania.prg208_1_exam.SearchActivity
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.net.URI
import java.util.concurrent.Executors


class APIService {

    suspend fun postImage(file: File): Pair<String?, ANError?> {

        var res: String? = null
        var err: ANError? = null

        AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                // do anything with progress
            }

            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    Log.i("debug", "Post: $response")
                    res = response
                }

                override fun onError(anError: ANError) {
                    err = anError
                }

            })

        return Pair(res, err)
    }

    fun getImages(){

    }
}