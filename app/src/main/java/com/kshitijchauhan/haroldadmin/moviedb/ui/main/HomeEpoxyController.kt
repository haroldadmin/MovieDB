package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.Typed3EpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.*

class HomeEpoxyController(
    private val callbacks: EpoxyCallbacks
) : Typed3EpoxyController<List<Movie>, List<Movie>, List<Movie>>() {

    @AutoModel
    lateinit var emptyPopularListModel: InfoTextModel_

    @AutoModel
    lateinit var emptyTopRatedListModel: InfoTextModel_

    override fun buildModels(popularMovies: List<Movie>?, topRatedMovies: List<Movie>?, searchResults: List<Movie>?) {
        if (searchResults == null) {
            buildHomeModel(popularMovies, topRatedMovies)
        } else {
            buildSearchModel(searchResults)
        }
    }

    private fun buildSearchModel(searchResults: List<Movie>?) {
        header {
            id("search-results")
            title("Search Results")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        when {
            searchResults == null -> infoText {
                id("search-away")
                id("search-info")
                text("Start typing. Search results will appear here.")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
            searchResults.isEmpty() -> infoText {
                id("no-results-found")
                text("No movies found for this query")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
            else -> searchResults.forEach { searchResult ->
                movieSearchResult {
                    with(searchResult) {
                        id(id)
                        movieId(id)
                        movieTitle(title)
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

    private fun buildHomeModel(popularMovies: List<Movie>?, topRatedMovies: List<Movie>?) {
        header {
            id("popular")
            title("Popular")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        if (popularMovies.isNullOrEmpty()) {
            emptyPopularListModel
                .text("No movies are popular right now. The world has changed, people don't like movies.")
                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                .also { add(it) }

        } else {
            popularMovies.forEach { popularMovie ->
                movie {
                    id(popularMovie.id)
                    movieId(popularMovie.id)
                    posterUrl(popularMovie.posterPath)
                    transitionName("poster-${popularMovie.id}")
                    clickListener { model, _, clickedView, _ ->
                        callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                    }
                }
            }
        }

        header {
            id("top-rated")
            title("Top Rated")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        if (topRatedMovies.isNullOrEmpty()) {
            emptyTopRatedListModel
                .text("No movies have good ratings right now.")
                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                .also { add(it) }
        } else {
            topRatedMovies.forEach { topRatedMovie ->
                movie {
                    id(topRatedMovie.id)
                    movieId(topRatedMovie.id)
                    posterUrl(topRatedMovie.posterPath)
                    transitionName("poster-${topRatedMovie.id}")
                    clickListener { model, _, clickedView, _ ->
                        callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                    }
                }
            }
        }
    }
}