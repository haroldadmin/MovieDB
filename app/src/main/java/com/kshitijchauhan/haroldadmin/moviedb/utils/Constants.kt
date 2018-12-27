package com.kshitijchauhan.haroldadmin.moviedb.utils

object Constants {

    val KEY_SESSION_ID = "session-id"

    val Genres = mapOf(
        19 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV MovieSearchResult",
        53 to "Thriller",
        10752 to "War",
        37 to "Western"
    )

    /**
     * "poster_sizes": [

    ]
     */

    /**
     * "backdrop_sizes": [

    ]
     */

    val availableBackdropSizes = mapOf<Int, String>(
        300 to "w300",
        780 to "w780",
        1280 to "w1280",
        0 to "original"
    )

    val availablePosterSizes = listOf(
        92 to "w92",
        154 to "w154",
        185 to "w185",
        342 to "w342",
        500 to "w500",
        780 to "w780",
        0 to "original"
    )

    val KEY_MOVIE_ID = "movie-id"
}