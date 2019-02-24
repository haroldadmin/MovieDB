package com.kshitijchauhan.haroldadmin.moviedb.ui.actor_details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteView
import kotlinx.android.synthetic.main.actor_details_fragment.*
import kotlinx.android.synthetic.main.actor_details_fragment.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ActorDetailsFragment : BaseFragment(), MVRxLiteView<UIState.ActorDetailsScreenState> {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val safeArgs: ActorDetailsFragmentArgs by navArgs()

    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }

    private val actorDetailsEpoxyController: ActorDetailsEpoxyController by inject()

    override val associatedUIState: UIState by lazy {
        UIState.ActorDetailsScreenState(
            this.arguments?.getInt(Constants.KEY_ACTOR_ID, -1) ?: -1,
            actorResource = Resource.Loading()
        )
    }

    private val actorDetailsViewModel: ActorDetailsViewModel by viewModel {
        parametersOf(safeArgs.actorIdArg, associatedUIState)
    }

    override fun updateToolbarTitle() {
        // Toolbar title will be updated when actor details are available
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        val view = inflater.inflate(R.layout.actor_details_fragment, container, false)
        ViewCompat.setTransitionName(view.ivActorPhoto, safeArgs.transitionNameArg)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvActorDetails.apply {
            layoutManager = LinearLayoutManager(context)
            setController(actorDetailsEpoxyController)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actorDetailsViewModel.apply {
            message.observe(viewLifecycleOwner, Observer { message ->
                mainViewModel.showSnackbar(message)
            })

            state.observe(viewLifecycleOwner, Observer { state ->
                renderState(state)
            })
        }
    }

    override fun renderState(state: UIState.ActorDetailsScreenState) {
        actorDetailsEpoxyController.setData(state)
        when (val resource = state.actorResource) {
            is Resource.Success -> {
                handleResourceSuccess(resource.data)
            }
            is Resource.Error -> {
                handleResourceError()
            }
            else -> Unit
        }.safe
    }

    private fun handleResourceSuccess(actor: Actor) {
        mainViewModel.updateToolbarTitle(actor.name)
        glideRequestManager
            .load(actor.profilePictureUrl)
            .apply {
                RequestOptions()
                    .fallback(R.drawable.ic_round_account_circle_24px)
                    .error(R.drawable.ic_round_account_circle_24px)
                    .placeholder(R.drawable.ic_round_account_circle_24px)
                    .circleCrop()
            }
            .listener(object : RequestListener<Drawable> {
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

    private fun handleResourceError() {
        startPostponedEnterTransition()
        mainViewModel.updateToolbarTitle(getString(R.string.actor_name_error))
        glideRequestManager.load(R.drawable.ic_round_account_circle_24px)
            .into(ivActorPhoto)
        tvActorName.text = getString(R.string.actor_name_error)
    }
}
