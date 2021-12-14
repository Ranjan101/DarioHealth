package com.dario.health.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteMovie(movie: MovieListEntity)

    @Query("SELECT * FROM moviedetail")
    fun getAllFavoriteMoviesList(): List<MovieListEntity>

    @Query("DElETE FROM moviedetail where imdbID=:imdbID")
    fun deleteFavoriteMovieByimdbID(imdbID: String): Int

}