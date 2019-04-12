package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteView
import kotlinx.android.synthetic.main.fragment_in_theatres.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.util.*
import kotlin.math.roundToInt

class InTheatresFragment : BaseFragment(), MVRxLiteView<UIState.InTheatresScreenState> {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val countryCode: String by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
            .getString(com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_COUNTRY_CODE, Locale.getDefault().country)
    }
    private val countryName: String by lazy {
        get<SharedPreferences>().getString(com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_COUNTRY_NAME, Locale.getDefault().displayCountry)
    }

    override val initialState: UIState by lazy {
        UIState.InTheatresScreenState(Resource.Loading(), countryCode, countryName)
    }

    private val inTheatresViewModel: InTheatresViewModel by viewModel {
        parametersOf(initialState)
    }

    private val callbacks = object : EpoxyCallbacks {
        override fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?) {
            InTheatresFragmentDirections.actionInTheatresFragmentToMovieDetailsFragment()
                .apply {
                    movieIdArg = id
                    isAuthenticatedArg = mainViewModel.isAuthenticated
                    transitionNameArg = transitionName
                }
                .also { action ->
                    sharedView?.let {
                        val extras = FragmentNavigatorExtras(it to transitionName)
                        findNavController().navigate(action, extras)
                    } ?: run {
                        findNavController().navigate(action)
                    }
                }
        }
    }

    private val glideRequestManager: RequestManager by inject(named("fragment-glide-request-manager")) {
        parametersOf(this)
    }

    private val inTheatresEpoxyController: InTheatresEpoxyController by inject {
        parametersOf(callbacks, glideRequestManager)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle(getString(R.string.title_in_theatres_screen))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_in_theatres, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inTheatresViewModel.apply {
            getMoviesInTheatres()
            state.observe(viewLifecycleOwner, Observer { state ->
                renderState(state)
            })

            message.observe(viewLifecycleOwner, Observer { message ->
                if (message.actionText != null && message.action == null) {
                    mainViewModel.showSnackbar(message.message, message.actionText, View.OnClickListener {
                        findNavController().navigate(R.id.settingsFragment)
                    })
                }
            })

            forceRefreshInTheatresCollection()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        rvMovies.apply {
            val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
            val space = resources.getDimension(R.dimen.movie_grid_item_space)
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            setController(inTheatresEpoxyController)
        }
    }

    override fun renderState(state: UIState.InTheatresScreenState) {
        inTheatresEpoxyController.setData(state)
        if (state.inTheatresMoviesResource is Resource.Success && state.inTheatresMoviesResource.data.isEmpty()) {
            mainViewModel.showSnackbar(
                R.string.change_country_message,
                R.string.action_settings,
                Snackbar.LENGTH_LONG,
                View.OnClickListener {
                    findNavController().navigate(R.id.settingsFragment)
                }
            )
        }
    }
}
