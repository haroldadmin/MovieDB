package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.jakewharton.rxbinding2.internal.Notification
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_searchbox.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class HomeFragment : BaseFragment() {

    private val TAG_GET_POPULAR_MOVIES = "get-popular-movies"
    private val TAG_GET_TOP_RATED_MOVIES = "get-top-rated-movies"

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val homeViewModel: HomeViewModel by viewModel()
    private val glideRequestManager: RequestManager by inject {
        parametersOf(this)
    }
    private val popularMoviesAdapter: MoviesListAdapter by inject {
        parametersOf(glideRequestManager, { id: Int, transitionName: String, sharedView: View ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        })
    }
    private val topRatedMoviesAdapter: MoviesListAdapter by inject {
        parametersOf(glideRequestManager, { id: Int, transitionName: String, sharedView: View ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
        })
    }

    private val onPause: PublishRelay<Any> = PublishRelay.create()

    override val associatedUIState: UIState = UIState.HomeScreenState

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle("Home")
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel.apply {

            if (popularMoviesUpdate.value == null) {
                mainViewModel.addLoadingTask(LoadingTask(TAG_GET_POPULAR_MOVIES, this@HomeFragment.viewLifecycleOwner))
                getPopularMovies()
            }

            if (topRatedMoviesUpdate.value == null) {
                mainViewModel.addLoadingTask(LoadingTask(TAG_GET_TOP_RATED_MOVIES, this@HomeFragment.viewLifecycleOwner))
                getTopRatedMovies()
            }

            popularMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                mainViewModel.completeLoadingTask(TAG_GET_POPULAR_MOVIES, viewLifecycleOwner)
                popularMoviesAdapter.submitList(newList)
            })

            topRatedMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                mainViewModel.completeLoadingTask(TAG_GET_TOP_RATED_MOVIES, viewLifecycleOwner)
                topRatedMoviesAdapter.submitList(newList)
            })
        }

        mainViewModel.updateToolbarTitle(getString(R.string.app_name))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        setupRecyclerViews()
        setupSearchBox()
    }

    private fun setupSearchBox() {
        RxView.focusChanges(searchBox.etSearchBox)
            .mergeWith(RxView.clicks(searchBox.searchIcon)
                .map { true }
            )
            .map { isFocused ->
                if (isFocused)
                    mainViewModel.updateStateTo(UIState.SearchScreenState)
            }
            .takeUntil(onPause)
            .subscribe()
    }

    private fun setupRecyclerViews() {
        val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
        val space = resources.getDimension(R.dimen.movie_grid_item_space)

        popularMoviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration((space).roundToInt()))
            adapter = popularMoviesAdapter
        }

        topRatedMoviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration((space).roundToInt()))
            adapter = topRatedMoviesAdapter
        }
    }

    override fun onPause() {
        super.onPause()
        onPause.accept(Notification.INSTANCE)
    }
}
