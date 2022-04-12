package no.kristiania.prg208_1_exam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.kristiania.prg208_1_exam.models.Post
import no.kristiania.prg208_1_exam.repository.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SearchViewModel(private val repository: Repository): ViewModel() {

    val postResponse: MutableLiveData<Response<String>> = MutableLiveData()
    val allPosts: MutableLiveData<Response<List<Post>>> = MutableLiveData()

    fun getAllPosts() {
        viewModelScope.launch {
            val response = repository.getAllPosts()
            allPosts.value = response
        }
    }

    fun postImage(image: MultipartBody.Part, fullName: RequestBody) {
        viewModelScope.launch {
            val response = repository.postImage(image, fullName)
            postResponse.value = response
        }
    }

}