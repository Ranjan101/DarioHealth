package com.dario.health.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dario.health.repository.DarioHealthMovieRepository

class MovieViewModelFactory(private val repository: DarioHealthMovieRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(repository) as T
    }

}