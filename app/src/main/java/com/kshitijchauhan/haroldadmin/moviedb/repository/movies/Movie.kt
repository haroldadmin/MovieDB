package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
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
    @ColumnInfo(name = "genre_ids")
    val genreIds: List<Int>,
    @ColumnInfo(name = "is_adult")
    val isAdult: Boolean,
    @ColumnInfo(name = "budget")
    val budget: Long?,
    @ColumnInfo(name = "revenue")
    val revenue: Long?,
    @ColumnInfo(name = "genres")
    val genres: List<String>?
)

@Entity(
    tableName = "account_states",
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"])]
)
data class AccountState(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "is_watchlisted")
    val isWatchlisted: Boolean,
    @ColumnInfo(name = "is_favourited")
    val isFavourited: Boolean,
    @ColumnInfo(name = "movie_id")
    val movieId: Int
)

@Entity(
    tableName = "casts",
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Cast(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "cast_members")
    val castMembersIds: List<Int>,
    @Ignore
    val castMembers: List<Actor>
)