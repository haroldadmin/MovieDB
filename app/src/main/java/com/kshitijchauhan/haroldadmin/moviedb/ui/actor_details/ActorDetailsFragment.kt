package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.mikepenz.itemanimators.AlphaInAnimator
import kotlinx.android.synthetic.main.actor_details_fragment.*
import kotlinx.android.synthetic.main.actor_details_fragment.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ActorDetailsFragment : BaseFragment() {

    private val actorDetailsViewModel: ActorDetailsViewModel by viewModel {
        parametersOf(arguments?.getInt(Constants.KEY_ACTOR_ID) ?: -1)
    }

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val actorDetailsEpoxyController = ActorDetailsEpoxyController()

    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }

    override val associatedUIState: UIState = UIState.ActorDetailsScreenState(
        this.arguments?.getInt(Constants.KEY_ACTOR_ID, -1) ?: -1
    )

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        // Toolbar title will be updated when actor details are available
    }

    companion object {
        fun newInstance(actorId: Int, transtitionName: String): ActorDetailsFragment {
            val newInstance = ActorDetailsFragment()
            newInstance.arguments = Bundle().apply {
                putInt(Constants.KEY_ACTOR_ID, actorId)
                putString(Constants.KEY_TRANSITION_NAME, transtitionName)
            }
            return newInstance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        val view = inflater.inflate(R.layout.actor_details_fragment, container, false)
        ViewCompat.setTransitionName(view.ivActorPhoto, arguments?.getString(Constants.KEY_TRANSITION_NAME))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvActorDetails.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = AlphaInAnimator()
            setController(actorDetailsEpoxyController)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actorDetailsEpoxyController.setData(null)
        actorDetailsViewModel.getActorDetails()
        actorDetailsViewModel.apply {
            message.observe(viewLifecycleOwner, Observer { message ->
                mainViewModel.showSnackbar(message)
            })

            actor.observe(viewLifecycleOwner, Observer { actor ->
                updateView(actor)
                actorDetailsEpoxyController.setData(actor)
            })
        }
    }

    private fun updateView(actor: Actor) {
        mainViewModel.updateToolbarTitle(actor.name)
        glideRequestManager
            .load(actor.profilePictureUrl)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            })
            .into(ivActorPhoto)
        tvActorName.text = actor.name
    }

}
