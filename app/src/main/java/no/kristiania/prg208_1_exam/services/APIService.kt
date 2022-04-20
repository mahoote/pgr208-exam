package no.kristiania.prg208_1_exam.services

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import no.kristiania.prg208_1_exam.SearchActivity
import no.kristiania.prg208_1_exam.models.ResultImage
import java.io.File


class APIService {

    fun postImage(activity: SearchActivity, file: File) {

        AndroidNetworking.upload("http://api-edu.gtl.ai/api/v1/imagesearch/upload")
            .addMultipartFile("image", file)
            .addMultipartParameter("key", "value")
            .addHeaders("Connection", "close")
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->

            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    activity.onSuccessfulPost(response)
                }

                override fun onError(anError: ANError) {
                    activity.onErrorResponse(anError)
                }
            })
    }

    fun getImages(activity: SearchActivity, searchEngine: String, url: String){
        AndroidNetworking.get("http://api-edu.gtl.ai/api/v1/imagesearch/{searchEngine}")
            .addPathParameter("searchEngine", searchEngine)
            .addQueryParameter("url", url)
            .addHeaders("Connection", "close")
            .build()
            .getAsObjectList(ResultImage::class.java, object : ParsedRequestListener<List<ResultImage?>> {
                override fun onResponse(images: List<ResultImage?>) {
                    activity.onSuccessfulGet(images)
                }

                override fun onError(anError: ANError) {
                    activity.onErrorResponse(anError)
                }
            })
    }
}