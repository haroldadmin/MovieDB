package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import com.airbnb.epoxy.TypedEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.header
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.infoText
import com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details.mainText

class ActorDetailsEpoxyController: TypedEpoxyController<Actor>() {

    override fun buildModels(actor: Actor?) {
        header {
            id("biography")
            title("Biography")
        }

        actor?.let {
            if (it.biography.isNullOrBlank()) {
                infoText {
                    id(it.id)
                    text("We can't find this information about this actor")
                }
            } else {
                mainText {
                    id(it.id)
                    text(it.biography)
                }
            }
        }
    }
}