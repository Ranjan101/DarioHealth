package com.dario.health.database

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.request.RequestOptions

import com.bumptech.glide.Glide

import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.target.Target


@Entity(tableName = "moviedetail")
data class MovieListEntity(
    @PrimaryKey()
    var imdbID: String,
    var Title: String,
    var Year: String,
    var Type: String,
    var Poster: String,
    var isFavoriteMovie: Boolean = false
)

