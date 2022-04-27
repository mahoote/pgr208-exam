package no.kristiania.prg208_1_exam.services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import no.kristiania.prg208_1_exam.fragments.UploadImageFragment
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.runnables.FetchImagesRunnable
import java.io.File
import java.time.Duration
import java.time.LocalDateTime


class ApiService {

    val path: String = "http://api-edu.gtl.ai/api/v1/imagesearch"

    fun postImage(fragment: UploadImageFragment, file: File) {
        AndroidNetworking.upload("$path/upload")
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .addHeaders("Connection", "close")
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                Log.d("Response", "bytes uploaded: $bytesUploaded / total bytes: $totalBytes")
            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    fragment.onSuccessfulPost(response)
                }

                override fun onError(anError: ANError) {
                    fragment.onErrorResponse(anError)
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getImages(runnable: FetchImagesRunnable, fragment: UploadImageFragment, searchEngine: String, url: String){
        val startThreadTime = LocalDateTime.now()

        Log.d("r_debug", "getImages: GET images from $searchEngine")

        AndroidNetworking.get("$path/{searchEngine}")
            .addPathParameter("searchEngine", searchEngine)
            .addQueryParameter("url", url)
            .addHeaders("Accept-Encoding", "identity")
            .build()
            .setDownloadProgressListener {bytesDownloaded, totalBytes ->
                Log.d("Response", "bytes downloaded: $bytesDownloaded / total bytes: $totalBytes")

            }
            .getAsObjectList(ResultImage::class.java, object : ParsedRequestListener<ArrayList<ResultImage?>> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(images: ArrayList<ResultImage?>) {

                    val endThreadTime = LocalDateTime.now()
                    val runnableUseTime = Duration.between(startThreadTime, endThreadTime).toMillis()
                    Log.d("r_debug", "onSuccessfulPost: Thread $searchEngine used $runnableUseTime milliseconds")
                    Log.d("m_debug", "onResponse: $images")

                    runnable.onSuccessfulGet(images, searchEngine)
                }

                override fun onError(anError: ANError) {
                    fragment.onErrorResponse(anError)
                }
            })
    }
}