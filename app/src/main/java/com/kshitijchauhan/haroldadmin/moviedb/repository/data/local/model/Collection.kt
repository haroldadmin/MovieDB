package com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey
    val name: String,
    @ColumnInfo(name="contents")
    val contents: List<Int>
)