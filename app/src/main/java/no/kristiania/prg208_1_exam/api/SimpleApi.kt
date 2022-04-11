package no.kristiania.prg208_1_exam.api

import android.net.Uri
import no.kristiania.prg208_1_exam.models.Post
import no.kristiania.prg208_1_exam.models.ResultImage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.net.URL

interface SimpleApi {

    @GET("/posts")
    suspend fun getAllPosts(): Response<List<Post>>

    @POST("/upload")
    suspend fun postImage(@Body image: Uri): Response<URL>

}