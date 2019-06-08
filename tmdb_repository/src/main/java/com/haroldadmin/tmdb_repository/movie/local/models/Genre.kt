package com.haroldadmin.tmdb_repository.movie.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String
)