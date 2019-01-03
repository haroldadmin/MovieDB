package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import androidx.recyclerview.widget.DiffUtil

class MoviesDiffUtil: DiffUtil.ItemCallback<MovieGridItem>() {
    override fun areItemsTheSame(oldItem: MovieGridItem, newItem: MovieGridItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieGridItem, newItem: MovieGridItem): Boolean {
        return oldItem == newItem
    }
}