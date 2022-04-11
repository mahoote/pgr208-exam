package no.kristiania.prg208_1_exam.api

import no.kristiania.prg208_1_exam.models.Post
import retrofit2.Response
import retrofit2.http.GET

interface SimpleApi {

    @GET("posts")
    suspend fun getAllPosts(): Response<List<Post>>

}