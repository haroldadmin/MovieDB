package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionNames.FAVOURITES_NAME
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionNames.IN_THEATRES_NAME
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionNames.POPULAR_NAME
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionNames.TOP_RATED_NAME
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionNames.WATCHLIST_NAME
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie

@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey
    val name: String,
    @ColumnInfo(name="contents")
    val contents: List<Int>
) {
    @Ignore
    var movies: List<Movie>? = null
}

object CollectionNames {
    const val FAVOURITES_NAME = "favourite"
    const val WATCHLIST_NAME = "watchlist"
    const val POPULAR_NAME = "popular"
    const val TOP_RATED_NAME = "top-rated"
    const val IN_THEATRES_NAME = "in-theatres"
}

sealed class CollectionType(val name: String) {
    object Favourite: CollectionType(FAVOURITES_NAME)
    object Watchlist: CollectionType(WATCHLIST_NAME)
    object Popular: CollectionType(POPULAR_NAME)
    object TopRated: CollectionType(TOP_RATED_NAME)
    object InTheatres: CollectionType(IN_THEATRES_NAME)
}
