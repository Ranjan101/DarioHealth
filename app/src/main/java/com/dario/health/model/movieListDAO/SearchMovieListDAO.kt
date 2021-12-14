package com.dario.health.model.movieListDAO

import com.dario.health.database.MovieListEntity

data class SearchMovieListDAO(
    val Response: String,
    val Search: List<MovieListEntity>,
    val totalResults: String
)