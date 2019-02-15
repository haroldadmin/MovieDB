package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import com.airbnb.epoxy.Typed3EpoxyController
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.*
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class HomeEpoxyController(
    private val callbacks: EpoxyCallbacks,
    private val glide: RequestManager
) : Typed3EpoxyController<Resource<List<Movie>>, Resource<List<Movie>>, Resource<List<Movie>>>() {

    override fun buildModels(
        popularMovies: Resource<List<Movie>>?,
        topRatedMovies: Resource<List<Movie>>?,
        searchResults: Resource<List<Movie>>?
    ) {
        if (searchResults == null) {
            buildHomeModel(popularMovies, topRatedMovies)
        } else {
            buildSearchModel(searchResults)
        }
    }

    private fun buildSearchModel(searchResults: Resource<List<Movie>>?) {
        header {
            id("search-results")
            title("Search Results")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        searchResults?.let {
            when (searchResults) {
                is Resource.Success -> {
                    when {
                        searchResults.data.isEmpty() -> {
                            infoText {
                                id("no-results-found")
                                text("No movies found for this query")
                                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                            }
                        }
                        else -> searchResults.data.forEach { searchResult ->
                            movieSearchResult {
                                with(searchResult) {
                                    id(id)
                                    movieId(id)
                                    movieTitle(title)
                                    glide(glide)
                                    posterUrl(posterPath)
                                    transitionName("poster-$id")
                                    clickListener { model, _, clickedView, _ ->
                                        callbacks.onMovieItemClicked(model.movieId!!, model.transitionName, clickedView)
                                    }
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    infoText {
                        id("error-search-results")
                        text("Error getting search results")
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    }
                }
                is Resource.Loading -> {
                    loading {
                        id("load-search-results")
                        description("Loading Search results")
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    }
                }
            }.safe
        }
    }

    private fun buildHomeModel(popularMovies: Resource<List<Movie>>?, topRatedMovies: Resource<List<Movie>>?) {
        header {
            id("popular")
            title("Popular")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        when (popularMovies) {
            is Resource.Success -> {
                popularMovies.data.forEach { popularMovie ->
                    movie {
                        id(popularMovie.id)
                        movieId(popularMovie.id)
                        glide(glide)
                        posterUrl(popularMovie.posterPath)
                        transitionName("poster-${popularMovie.id}")
                        clickListener { model, _, clickedView, _ ->
                            callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                        }
                    }
                }
            }
            is Resource.Error -> {
                infoText {
                    id("error-popular-movies")
                    text("Error getting Popular movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> {
                loading {
                    id("load-popular-movies")
                    description("Loading Popular movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            null -> Unit
        }.safe

        header {
            id("top-rated")
            title("Top Rated")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        when (topRatedMovies) {
            is Resource.Success -> {
                topRatedMovies.data.forEach { topRatedMovie ->
                    movie {
                        id(topRatedMovie.id)
                        movieId(topRatedMovie.id)
                        glide(glide)
                        posterUrl(topRatedMovie.posterPath)
                        transitionName("poster-${topRatedMovie.id}")
                        clickListener { model, _, clickedView, _ ->
                            callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                        }
                    }
                }
            }
            is Resource.Error -> {
                infoText {
                    id("error-top-rated-movies")
                    text("Error getting Top Rated movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> {
                loading {
                    id("load-top-rated-movies")
                    description("Loading Top Rated movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            null -> Unit
        }.safe
    }
}