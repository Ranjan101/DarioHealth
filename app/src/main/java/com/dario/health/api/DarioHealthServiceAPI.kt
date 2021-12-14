package com.cheezycode.randomquote.api

import com.dario.health.model.movieListDAO.SearchMovieListDAO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DarioHealthServiceAPI {

    @GET("?")
    suspend fun getMovieList(
        @Query("apikey") apikey: String,
        @Query("s") queryString: String
    ): Response<SearchMovieListDAO>


}