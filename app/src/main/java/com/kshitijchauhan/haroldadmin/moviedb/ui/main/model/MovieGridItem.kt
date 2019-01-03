package com.kshitijchauhan.haroldadmin.moviedb.ui.main.model

data class MovieGridItem(
    val id: Int,
    val name: String,
    val posterPath: String,
    val type: MovieTypes
)