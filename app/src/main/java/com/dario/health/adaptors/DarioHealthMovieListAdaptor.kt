package com.dario.health.adaptors

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.persistableBundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.dario.health.R
import com.dario.health.database.MovieListEntity
import com.dario.health.databinding.DarioHealthMoviesListAdaptorActivityMainBinding
import com.dario.health.BR;


class DarioHealthMovieListAdaptor( itemViewModels: ArrayList<MovieListEntity>) :
    RecyclerView.Adapter<DarioHealthMovieListAdaptor.BindableViewHolder>() {
    var itemViewModelList: List<MovieListEntity>? = null;

    init {
        this.itemViewModelList = itemViewModels;
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding: DarioHealthMoviesListAdaptorActivityMainBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.dario_health_movies_list_adaptor_activity_main,
            parent,
            false
        )
        return BindableViewHolder(binding)
    }


    override fun getItemCount(): Int = itemViewModelList!!.size

    override fun onBindViewHolder(binding: BindableViewHolder, position: Int) {


        itemViewModelList?.let {
            binding.bind(it.get(position))

        }
    }

    interface OnFavoritesMovieItemClicked {
        fun onFavoritesMovieItemClicked(position: Int)
    }

    interface OnMovieViewClicked {
        fun onMovieViewClicked(position: Int)
    }

    private var onMovieViewClicked: OnMovieViewClicked? = null
    private var onFavoritesMovieItemClicked: OnFavoritesMovieItemClicked? = null
    fun setOnFavoritesMovieItemClicked(onFavoritesMovieItemClicked: OnFavoritesMovieItemClicked?) {
        this.onFavoritesMovieItemClicked = onFavoritesMovieItemClicked
    }

    fun setOnMovieViewClicked(onMovieViewClicked: OnMovieViewClicked?) {
        this.onMovieViewClicked = onMovieViewClicked
    }

    inner class BindableViewHolder(private val binding: DarioHealthMoviesListAdaptorActivityMainBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(data: Any) {
            binding.setVariable(BR.movie, data)
            binding.executePendingBindings()
            if (itemViewModelList?.get(adapterPosition)?.isFavoriteMovie == true) {
                binding.imageViewMovieFavorites.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorPrimaryDark
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                );
            } else {
                binding.imageViewMovieFavorites.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.Grey
                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                );
            }
            binding.imageViewMovieFavorites.setOnClickListener(this)
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.image_view_movie_favorites -> {
                    onFavoritesMovieItemClicked?.run {
                        this.onFavoritesMovieItemClicked(adapterPosition)
                    }
                }
                else -> {
                    onMovieViewClicked?.run {
                        this.onMovieViewClicked(adapterPosition)
                    }
                }

            }

        }
    }


}