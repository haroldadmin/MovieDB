package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import com.airbnb.epoxy.TypedEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.header
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.infoText
import com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details.mainText
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class ActorDetailsEpoxyController: TypedEpoxyController<Resource<Actor>>() {

    override fun buildModels(resource: Resource<Actor>) {

        header {
            id("biography")
            title("Biography")
        }

        when (resource) {
            is Resource.Success -> {
                if (resource.data.biography.isNullOrBlank()) {
                    infoText {
                        id("empty-bio")
                        text("We can't find this information about this actor")
                    }
                } else {
                    mainText {
                        id(resource.data.id)
                        text(resource.data.biography)
                    }
                }
            }
            is Resource.Error -> {
                infoText {
                    id("error-info")
                    text(resource.errorMessage)
                }
            }
            is Resource.Loading -> {
                infoText {
                    id("loading-info")
                    text("Loading...")
                }
            }
        }.safe
    }
}