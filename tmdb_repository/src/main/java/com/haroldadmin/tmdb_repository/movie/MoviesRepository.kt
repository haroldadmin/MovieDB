package com.haroldadmin.tmdb_repository.movie

import com.haroldadmin.tmdb_repository.NetworkBoundResource
import com.haroldadmin.tmdb_repository.actor.local.Actor
import com.haroldadmin.tmdb_repository.movie.local.LocalMoviesSource
import com.haroldadmin.tmdb_repository.movie.local.models.Movie
import com.haroldadmin.tmdb_repository.movie.remote.RemoteMoviesSource
import com.haroldadmin.tmdb_repository.movie.remote.models.MovieStatesResponse
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(
    private val localMoviesSource: LocalMoviesSource,
    private val remoteMoviesSource: RemoteMoviesSource
) {

    fun getMovieDetails(movieId: Int, coroutineScope: CoroutineScope) =
        object : NetworkBoundResource<Movie>(coroutineScope) {
            override suspend fun fetchFromNetwork(): Resource<Movie> = withContext(Dispatchers.IO) {
                remoteMoviesSource.getMovieDetails(movieId)
            }

            override suspend fun fetchFromDisk(): Resource<Movie> = withContext(Dispatchers.IO) {
                localMoviesSource.getMovieDetails(movieId)
            }

            override suspend fun shouldRefreshDiskModel(
                diskResource: Resource<Movie>,
                networkResource: Resource<Movie>
            ): Boolean = withContext(Dispatchers.IO) {
                when {
                    diskResource is Resource.Error && networkResource is Resource.Success -> true
                    diskResource is Resource.Success && networkResource is Resource.Success
                            && diskResource.data != networkResource.data -> true
                    else -> false
                }
            }

            override suspend fun saveToDisk(resource: Movie) = withContext(Dispatchers.IO) {
                localMoviesSource.saveMovie(resource)
            }
        }

    fun getAccountStatesForMovie(movieId: Int, coroutineScope: CoroutineScope) =
        object : NetworkBoundResource<MovieStatesResponse>(coroutineScope) {
            override suspend fun fetchFromNetwork(): Resource<MovieStatesResponse> = withContext(Dispatchers.IO) {
                remoteMoviesSource.getMovieAccountState(movieId)
            }

            override suspend fun fetchFromDisk(): Resource<MovieStatesResponse> = withContext(Dispatchers.IO) {
                localMoviesSource.getMovieAccountState(movieId)
            }

            override suspend fun shouldRefreshDiskModel(
                diskResource: Resource<MovieStatesResponse>,
                networkResource: Resource<MovieStatesResponse>
            ): Boolean = withContext(Dispatchers.IO) {
                when (networkResource) {
                    is Resource.Success -> true
                    else -> false
                }
            }

            override suspend fun saveToDisk(resource: MovieStatesResponse) {
                localMoviesSource.updateMovieState(movieId, resource)
            }
        }

    fun getMovieCast(movieId: Int, coroutineScope: CoroutineScope) =
        object : NetworkBoundResource<List<Actor>>(coroutineScope) {
            override suspend fun fetchFromNetwork(): Resource<List<Actor>> = withContext(Dispatchers.IO) {
                remoteMoviesSource.getActorsInMovie(movieId)
            }

            override suspend fun fetchFromDisk(): Resource<List<Actor>> = withContext(Dispatchers.IO) {
                localMoviesSource.getActorsInMovie(movieId)
            }

            override suspend fun shouldRefreshDiskModel(
                diskResource: Resource<List<Actor>>,
                networkResource: Resource<List<Actor>>
            ): Boolean = withContext(Dispatchers.IO) {
                when {
                    networkResource is Resource.Success && diskResource is Resource.Success && networkResource.data != diskResource.data -> true
                    networkResource is Resource.Success && diskResource is Resource.Error -> true
                    else -> false
                }
            }

            override suspend fun saveToDisk(resource: List<Actor>) = withContext(Dispatchers.IO) {
                localMoviesSource.saveActorsInMovie(movieId, resource)
            }

        }
}
