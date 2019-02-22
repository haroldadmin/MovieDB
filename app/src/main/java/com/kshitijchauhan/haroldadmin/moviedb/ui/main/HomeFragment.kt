package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.jakewharton.rxbinding2.internal.Notification
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BackPressListener
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_searchbox.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class HomeFragment : BaseFragment(), BackPressListener {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    private val callbacks = object : EpoxyCallbacks {
        override fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?) {
            mainViewModel.updateStateTo(UIState.DetailsScreenState(
                movieId = id,
                transitionName = transitionName,
                sharedView = sharedView,
                movieResource = Resource.Loading(),
                accountStatesResource = Resource.Loading(),
                trailerResource = Resource.Loading(),
                castResource = listOf(Resource.Loading())
            ))
        }
    }

    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }

    private val homeEpoxyController by lazy { HomeEpoxyController(callbacks, glideRequestManager) }

    private val onDestroyView: PublishRelay<Any> = PublishRelay.create()

    override val associatedUIState: UIState = UIState.HomeScreenState

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle(getString(R.string.app_name))
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeEpoxyController.setData(Resource.Loading(), Resource.Loading(), null)
        homeViewModel.apply {

            if (popularMovies.value == null) {
                getPopularMovies()
            }

            if (topRatedMovies.value == null) {
                getTopRatedMovies()
            }

            popularMovies.observe(viewLifecycleOwner, Observer {
                updateEpoxyController()
            })

            topRatedMovies.observe(viewLifecycleOwner, Observer {
                updateEpoxyController()
            })

            searchResults.observe(viewLifecycleOwner, Observer {
                updateEpoxyController()
            })

            message.observe(viewLifecycleOwner, Observer { message ->
                mainViewModel.showSnackbar(message)
            })

            forceRefreshCollection(CollectionType.Popular)
            forceRefreshCollection(CollectionType.TopRated)
        }

        updateToolbarTitle()
        mainViewModel.setBackPressListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        rvHome.apply {
            val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
            val space = resources.getDimension(R.dimen.movie_grid_item_space)
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            setController(homeEpoxyController)
        }
        (view.parent as ViewGroup).doOnPreDraw {
            startPostponedEnterTransition()
        }
        setupSearchBox()
        // To make sure the search box does not have focus
        getView()?.requestFocus()
    }

    private fun setupSearchBox() {
        RxTextView.textChangeEvents(searchBox.etSearchBox)
            .debounce(300, TimeUnit.MILLISECONDS)
            .map { event -> event.text().toString() }
            .takeUntil(onDestroyView)
            .doOnNext { query ->
                homeViewModel.getSearchResultsForQuery(query)
            }
            .subscribe()
    }

    private fun updateEpoxyController() {
        with(homeViewModel) {
            homeEpoxyController.setData(
                popularMovies.value,
                topRatedMovies.value,
                searchResults.value
            )
        }
    }

    override fun onBackPressed(): Boolean {
        return with(homeViewModel) {
            searchResults.value?.let {
                clearSearchResults()
                searchBox.etSearchBox.setText("")
                this@HomeFragment.view?.requestFocus()
                false
            } ?: true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyView.accept(Notification.INSTANCE)
        mainViewModel.setBackPressListener(null)
    }
}
