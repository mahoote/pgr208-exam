package no.kristiania.prg208_1_exam.repository

import no.kristiania.prg208_1_exam.api.RetrofitInstance
import no.kristiania.prg208_1_exam.models.ResultImage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ImageRepo {

    suspend fun postImage(image: MultipartBody.Part, fullName: RequestBody): Response<String> {
        return RetrofitInstance.api.postImage(fullName, image)
    }

    suspend fun getImage(searchEngine: String, url: String): Response<List<ResultImage>> {
        return RetrofitInstance.api.getImages(searchEngine, url)
    }

}