package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import android.view.View
import com.airbnb.epoxy.*
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieTrailer
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.ActorModel_
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.header
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.infoText
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.loading
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class DetailsEpoxyController(
    private val callbacks: DetailsEpoxyController.MovieDetailsCallbacks,
    private val glide: RequestManager
) : TypedEpoxyController<UIState.DetailsScreenState>() {

    interface MovieDetailsCallbacks {
        fun toggleMovieFavouriteStatus()
        fun toggleMovieWatchlistStatus()
        fun onActorItemClicked(id: Int, transitionName: String, sharedView: View?)
    }

    override fun buildModels(state: UIState.DetailsScreenState) {
        with(state) {
            buildAccountStatesResource(accountStatesResource)
            buildMovieResource(movieResource)
            buildCastResource(castResource)
//            buildTrailerResource(trailerResource)
        }
    }

    private fun buildMovieResource(resource: Resource<Movie>) {
        when (resource) {
            is Resource.Success -> {
                val movie = resource.data
                header {
                    id("description")
                    title("Description")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                mainText {
                    id("overview")
                    text(movie.overview)
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Error -> {
                header {
                    id("description")
                    title("Description")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                mainText {
                    id("overview")
                    text("We can't find a description for this movie")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> {
                header {
                    id("description")
                    title("Description")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                loading {
                    id("load-movie-description")
                    description("Loading Description")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
        }.safe
    }

    private fun buildAccountStatesResource(resource: Resource<AccountState>) {
        when (resource) {
            is Resource.Success -> {
                val accountState = resource.data
                infoBar {
                    id("info")
                    accountStates(accountState)
                    watchListClickListener { _, _, _, _ ->
                        callbacks.toggleMovieWatchlistStatus()
                    }
                    favouritesClickListener { _, _, _, _ ->
                        callbacks.toggleMovieFavouriteStatus()
                    }
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Error -> Unit
            is Resource.Loading -> Unit
        }.safe
    }

    private fun buildTrailerResource(resource: Resource<MovieTrailer>) {
        when (resource) {
            is Resource.Success -> {
                header {
                    id("trailer")
                    title("Trailer")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                trailer {
                    id(resource.data.movieId)
                    trailerKey(resource.data.youtubeVideoKey)
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Error -> {
                header {
                    id("trailer")
                    title("Trailer")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                infoText {
                    id("trailer-empty")
                    text("We can't find a trailer for this movie")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> Unit
        }.safe
    }

    private fun buildCastResource(actors: List<Resource<Actor>>) {
        actors
            .filterIsInstance<Resource.Success<Actor>>()
            .takeIf { it.isNotEmpty() }
            ?.let { actorsList ->

                header {
                    id("cast")
                    title("Cast")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                carousel {
                    id("cast-carousel")
                    numViewsToShowOnScreen(this@DetailsEpoxyController.spanCount.toFloat().minus(1f).times(1.2f))
                    withModelsFrom(actorsList) { actorResource ->
                        ActorModel_()
                            .id(actorResource.data.id)
                            .actorId(actorResource.data.id)
                            .name(actorResource.data.name)
                            .glide(glide)
                            .pictureUrl(actorResource.data.profilePictureUrl)
                            .transitionName("actor-${actorResource.data.id}")
                            .clickListener { model, _, clickedView, _ ->
                                callbacks.onActorItemClicked(model.actorId!!, model.transitionName(), clickedView)
                            }
                    }
                }
            }
        }

    /** For use in the buildModels method of EpoxyController. A shortcut for creating a Carousel model, initializing it, and adding it to the controller.
     *
     */
    private inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
        CarouselModel_().apply {
            modelInitializer()
        }.addTo(this)
    }

    /** Add models to a CarouselModel_ by transforming a list of items into EpoxyModels.
     *
     * @param items The items to transform to models
     * @param modelBuilder A function that take an item and returns a new EpoxyModel for that item.
     */
    private inline fun <T> CarouselModelBuilder.withModelsFrom(
        items: List<T>,
        modelBuilder: (T) -> EpoxyModel<*>
    ) {
        models(items.map { modelBuilder(it) })
    }
}