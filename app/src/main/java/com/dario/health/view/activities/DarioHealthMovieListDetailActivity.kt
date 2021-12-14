package com.dario.health.view.activities

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dario.health.database.MovieListEntity
import com.dario.health.databinding.DarioHealthMoviesListDetailActivityBinding
import com.dario.health.view.loadImage
import com.google.gson.Gson

class DarioHealthMovieListDetailActivity : AppCompatActivity() {
    val TAG = DarioHealthMovieListDetailActivity::class.java.simpleName
    lateinit var layoutBinder: DarioHealthMoviesListDetailActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinder = DarioHealthMoviesListDetailActivityBinding.inflate(layoutInflater)
        setContentView(layoutBinder.root)
        setSupportActionBar(layoutBinder.toolbarMain)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.elevation = 0f
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val bundle: Bundle? = getIntent().extras
        if (bundle != null) {
            val strValue = bundle.getString("movieDetail")
            val mGson = Gson()
            val movieListEntity: MovieListEntity =
                mGson.fromJson(strValue, MovieListEntity::class.java);
            layoutBinder.txtViewTitle.setText(movieListEntity.Title)
            layoutBinder.txtViewType.setText(movieListEntity.Type)
            layoutBinder.txtViewYear.setText(movieListEntity.Year)
            Glide.with(this)
                .load(movieListEntity.Poster).apply(RequestOptions().fitCenter())
                .placeholder(com.dario.health.R.drawable.place_holder_detail_page)
                .into(layoutBinder.imageViewMoviePoster)
            if (movieListEntity?.isFavoriteMovie == true) {
                layoutBinder.imageViewMoviePosterFavorites.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        com.dario.health.R.color.colorPrimaryDark
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                );
            } else {
                layoutBinder.imageViewMoviePosterFavorites.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        com.dario.health.R.color.Grey
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                );
            }


        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}