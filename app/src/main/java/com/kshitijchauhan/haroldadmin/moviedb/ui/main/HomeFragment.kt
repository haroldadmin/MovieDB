package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.internal.Notification
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_searchbox.view.*
import kotlin.math.roundToInt

class HomeFragment : BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var popularMoviesAdapter: MoviesListAdapter
    private lateinit var topRatedMoviesAdapter: MoviesListAdapter

    private val onPause: PublishRelay<Any> = PublishRelay.create()

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModels()

        homeViewModel.apply {

            if (popularMoviesUpdate.value == null) {
                mainViewModel.setProgressBarVisible(true)
                getPopularMovies()
            }

            if (topRatedMoviesUpdate.value == null) {
                mainViewModel.setProgressBarVisible(true)
                getTopRatedMovies()
            }

            popularMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                mainViewModel.setProgressBarVisible(false)
                popularMoviesAdapter.submitList(newList)
            })

            topRatedMoviesUpdate.observe(viewLifecycleOwner, Observer { newList ->
                mainViewModel.setProgressBarVisible(false)
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

        popularMoviesAdapter =
                MoviesListAdapter(Glide.with(this)) { id, transitionName, sharedView ->
                    mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
                }

        topRatedMoviesAdapter =
                MoviesListAdapter(Glide.with(this)) { id, transitionName, sharedView ->
                    mainViewModel.updateStateTo(UIState.DetailsScreenState(id, transitionName, sharedView))
                }

        setupRecyclerViews()
        setupSearchBox()
    }

    private fun initViewModels() {
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
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
        val columns = getNumberOfColumns(resources.getDimension(R.dimen.movie_grid_poster_width))
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

    private fun getNumberOfColumns(itemWidthPx: Float): Int {
        val screenWidth = resources.displayMetrics.widthPixels
        return screenWidth.div(itemWidthPx).toInt()
    }

    override fun onPause() {
        super.onPause()
        onPause.accept(Notification.INSTANCE)
    }
}
