package com.dario.health.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dario.health.repository.DarioHealthMovieRepository

class FavoritesMovieViewModelFactory(private val repository: DarioHealthMovieRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesMovieViewModel(repository) as T
    }

}