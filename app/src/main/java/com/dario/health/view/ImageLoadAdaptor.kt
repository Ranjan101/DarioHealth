package com.dario.health.view

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dario.health.R
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("loadImage")
fun ShapeableImageView.loadImage(imageUrl: String?) {
    Glide.with(this.getContext())
        .load(imageUrl).apply(RequestOptions().circleCrop())
        .placeholder(R.drawable.ic_launcher)
        .into(this)
}