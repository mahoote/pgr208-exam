package no.kristiania.prg208_1_exam

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.kristiania.prg208_1_exam.models.Post
import no.kristiania.prg208_1_exam.repository.Repository
import retrofit2.Response
import java.net.URL

class SearchViewModel(private val repository: Repository): ViewModel() {

    val postResponse: MutableLiveData<Response<URL>> = MutableLiveData()
    val allPosts: MutableLiveData<Response<List<Post>>> = MutableLiveData()

    fun getAllPosts() {
        viewModelScope.launch {
            val response = repository.getAllPosts()
            allPosts.value = response
        }
    }

    fun postImage(image: Uri) {
        viewModelScope.launch {
            val response = repository.postImage(image)
            postResponse.value = response
        }
    }

}