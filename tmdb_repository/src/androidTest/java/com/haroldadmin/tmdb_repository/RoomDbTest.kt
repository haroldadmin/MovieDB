package com.haroldadmin.tmdb_repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.tmdb_repository.movie.local.MovieDao
import com.haroldadmin.tmdb_repository.movie.local.models.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class RoomDbTest {
    private lateinit var moviesDao: MovieDao
    private lateinit var roomDb: TMDbDatabase

    @Before
    fun setup() {
        roomDb = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            TMDbDatabase::class.java).build()
        moviesDao = roomDb.moviesDao()
    }

    @Test
    fun movieTest() = runBlocking {
//        moviesDao.saveMovie(Movie(1, "", "", "", "", 0.0, Date(0L), false, false, false, 0, 0))
        val movie: Movie = moviesDao.getMovie(1)
        assertEquals(1, movie.id)
        println(""""
            |XXXXXXXXXXX
            |XXXXXXXXXX
            |XXXXXXXXX
            |\n\n\n\n\n$movie XXXXXXXXXXXXXXXXX
            |XXXXXXXXXXXXXXXX
            |XXXXXXXX\n\n\n\n\n\n\n""".trimMargin())
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        roomDb.close()
    }
}

