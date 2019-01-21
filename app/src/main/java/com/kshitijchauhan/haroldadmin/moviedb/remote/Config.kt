package com.kshitijchauhan.haroldadmin.moviedb.remote

import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants

object Config {

    const val API_VERSION: Int = 3

    const val BASE_URL: String = "https://api.themoviedb.org/$API_VERSION/"

    const val BASE_IMAGE_URL: String = "https://image.tmdb.org/t/p/"

    const val DEFAULT_BACKDROP_SIZE: String = "w780"

    const val DEFAULT_POSTER_SIZE: String = "w342"

    const val SMALL_POSTER_SIZE: String = "w185"

    const val SMALL_PROFILE_PICTURE_SIZE: String = "w185"

}