package no.kristiania.prg208_1_exam.repository

import android.net.Uri
import no.kristiania.prg208_1_exam.api.RetrofitInstance
import no.kristiania.prg208_1_exam.models.Post
import retrofit2.Response
import retrofit2.http.Url
import java.net.URL

class Repository {

    suspend fun getAllPosts(): Response<List<Post>> {
        return RetrofitInstance.api.getAllPosts()
    }

    suspend fun postImage(image: Uri): Response<URL> {
        return RetrofitInstance.api.postImage(image)
    }

}