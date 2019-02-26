package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import android.os.Handler
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.*
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class InTheatresEpoxyController(
    private val callbacks: EpoxyCallbacks,
    private val glide: RequestManager,
    epoxyHandler: Handler
): TypedEpoxyController<UIState.InTheatresScreenState>(epoxyHandler, epoxyHandler) {

    override fun buildModels(state: UIState.InTheatresScreenState) {
        with(state) {
            header {
                id("in-theatres-header")
                title(state.countryName)
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            when (inTheatresMoviesResource) {
                is Resource.Success -> {
                    when {
                        inTheatresMoviesResource.data.isEmpty() -> infoText {
                            id("in-theatres-info")
                            text("We can't find any movies in theatres near you")
                            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                        }
                        else -> inTheatresMoviesResource.data.forEach { inTheatreMovie ->
                            movie {
                                id(inTheatreMovie.id)
                                movieId(inTheatreMovie.id)
                                glide(glide)
                                posterUrl(inTheatreMovie.posterPath)
                                transitionName("poster-${inTheatreMovie.id}")
                                clickListener { model, _, clickedView, _ ->
                                    callbacks.onMovieItemClicked(model.movieId!!, model.transitionName, clickedView)
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    infoText {
                        id("error-in-theatres-movies")
                        text("Error getting Movies in Theatres")
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    }
                }
                is Resource.Loading -> {
                    loading {
                        id("load-in-theatre-movies")
                        description("Loading In-Theatre movies")
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    }
                }
            }.safe
        }
    }

}