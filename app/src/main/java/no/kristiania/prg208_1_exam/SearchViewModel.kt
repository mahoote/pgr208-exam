package no.kristiania.prg208_1_exam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.repository.ImageRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SearchViewModel(private val imageRepo: ImageRepo): ViewModel() {

    val postResponse: MutableLiveData<Response<String>> = MutableLiveData()
    val getResponse: MutableLiveData<Response<List<ResultImage>>> = MutableLiveData()

    fun postImage(image: MultipartBody.Part, fullName: RequestBody) {
        viewModelScope.launch {
            val response = imageRepo.postImage(image, fullName)
            postResponse.value = response
        }
    }

    fun getImage(searchEngine: String, url: String){
        viewModelScope.launch {
            val response = imageRepo.getImage(searchEngine, url)
            getResponse.value = response
        }
    }

}