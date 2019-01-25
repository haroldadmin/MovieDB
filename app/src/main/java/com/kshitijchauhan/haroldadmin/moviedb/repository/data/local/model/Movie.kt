package com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "release_date")
    val releaseDate: Date,
    @ColumnInfo(name = "genre")
    val genre: String,
    @ColumnInfo(name = "watchlisted")
    val isWatchlisted: Boolean?,
    @ColumnInfo(name = "favourite")
    val isFavourited: Boolean?,
    @ColumnInfo(name = "trailer_url")
    val trailerUrl: String,
    @ColumnInfo(name = "cast_ids")
    val castIds: List<Int>
)