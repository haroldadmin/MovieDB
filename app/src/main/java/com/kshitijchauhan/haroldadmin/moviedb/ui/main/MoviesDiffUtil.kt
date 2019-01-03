package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.model.MovieGridItem

class MoviesDiffUtil: DiffUtil.ItemCallback<MovieGridItem>() {
    override fun areItemsTheSame(oldItem: MovieGridItem, newItem: MovieGridItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieGridItem, newItem: MovieGridItem): Boolean {
        return oldItem == newItem
    }
}