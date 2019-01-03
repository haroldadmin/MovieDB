package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import com.kshitijchauhan.haroldadmin.moviedb.ui.MovieItemType

data class MovieGridItem(
    val id: Int,
    val name: String,
    val posterPath: String,
    val type: MovieItemType
)