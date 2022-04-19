package no.kristiania.prg208_1_exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import no.kristiania.prg208_1_exam.repository.ImageRepo

class SearchViewModelFactory(private val imageRepo: ImageRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(imageRepo) as T
    }
}