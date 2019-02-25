package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import android.os.Handler
import com.airbnb.epoxy.TypedEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.header
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.infoText
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.loading
import com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details.mainText
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class ActorDetailsEpoxyController(
    epoxyHandler: Handler
): TypedEpoxyController<UIState.ActorDetailsScreenState>(epoxyHandler, epoxyHandler) {

    override fun buildModels(state: UIState.ActorDetailsScreenState) {
        with(state) {
            header {
                id("biography")
                title("Biography")
            }

            when (actorResource) {
                is Resource.Success -> {
                    if (actorResource.data.biography.isNullOrBlank()) {
                        infoText {
                            id("empty-bio")
                            text("We can't find this information about this actor")
                        }
                    } else {
                        mainText {
                            id(actorResource.data.id)
                            text(actorResource.data.biography)
                        }
                    }
                }
                is Resource.Error -> {
                    infoText {
                        id("error-info")
                        text(actorResource.errorMessage)
                    }
                }
                is Resource.Loading -> {
                    loading {
                        id("bio-loading")
                        description("Loading biography")
                    }
                }
            }.safe
        }
    }
}