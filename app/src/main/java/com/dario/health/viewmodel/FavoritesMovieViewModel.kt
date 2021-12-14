package com.dario.health.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dario.health.database.MovieListEntity
import com.dario.health.repository.DarioHealthMovieRepository
import com.dario.health.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesMovieViewModel(private val repository: DarioHealthMovieRepository) : ViewModel() {


    fun getAllFavoritesMovieList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFavoritesMovieList()
        }
    }

    fun setFavoritesMovieStatus(position: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setFavoritesMovieStatusDB(position!!)
        }
    }


    val liveDataMovieList: LiveData<Response<List<MovieListEntity>>>
        get() = repository.favoritesMovieLiveData


}