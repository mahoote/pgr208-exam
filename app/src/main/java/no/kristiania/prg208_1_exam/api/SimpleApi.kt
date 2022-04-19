package no.kristiania.prg208_1_exam.api

import no.kristiania.prg208_1_exam.models.ResultImage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi {

    @Multipart
    @POST("upload")
    suspend fun postImage(
        @Part("image") name: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<String>

    @GET("{searchEngine}")
    suspend fun getImages(
        @Path("searchEngine") searchEngine: String,
        @Query("url") url: String
    ): Response<List<ResultImage>>

}