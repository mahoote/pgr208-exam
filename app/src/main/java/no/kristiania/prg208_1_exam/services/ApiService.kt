package no.kristiania.prg208_1_exam.services

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import no.kristiania.prg208_1_exam.fragments.UploadImageFragment
import no.kristiania.prg208_1_exam.models.ResultImage
import org.json.JSONArray
import java.io.File


class ApiService {

    fun postImage(fragment: UploadImageFragment, file: File) {
        AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .addHeaders("Connection", "close")
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
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

    fun getImages(fragment: UploadImageFragment, searchEngine: String, url: String){
        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/{searchEngine}")
            .addPathParameter("searchEngine", searchEngine)
            .addQueryParameter("url", url)
            .addHeaders("Accept-Encoding", "identity")
            .build()
            .setDownloadProgressListener {bytesDownloaded, totalBytes ->
                Log.d("m_debug", "BytesDownloaded: $bytesDownloaded")
                Log.d("m_debug", "Total: $totalBytes")
            }
            .getAsObjectList(ResultImage::class.java, object : ParsedRequestListener<List<ResultImage?>> {
                override fun onResponse(images: List<ResultImage?>) {
                    Log.d("m_debug", "onResponse: $images")
                    fragment.onSuccessfulGet(images)
                }

                override fun onError(anError: ANError) {
                    fragment.onErrorResponse(anError)
                }
            })
    }
}