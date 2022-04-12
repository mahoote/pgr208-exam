package no.kristiania.prg208_1_exam.api

import no.kristiania.prg208_1_exam.models.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi {

    @GET("posts")
    suspend fun getAllPosts(): Response<List<Post>>

    @Multipart
    @POST("upload")
    suspend fun postImage(
        @Part("image") name: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<String>

}