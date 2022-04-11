package no.kristiania.prg208_1_exam.repository

import no.kristiania.prg208_1_exam.api.RetrofitInstance
import no.kristiania.prg208_1_exam.models.Post
import retrofit2.Response

class Repository {

    suspend fun getAllPosts(): Response<List<Post>> {
        return RetrofitInstance.api.getAllPosts()
    }

}