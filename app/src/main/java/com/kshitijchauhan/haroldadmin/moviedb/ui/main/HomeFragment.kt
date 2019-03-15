package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.internal.Notification
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BackPressListener
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_searchbox.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class HomeFragment :
    BaseFragment(),
    BackPressListener,
    MVRxLiteView<UIState.HomeScreenState> {

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val callbacks = object : EpoxyCallbacks {
        override fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?) {
            HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment()
                .apply {
                    movieIdArg = id
                    isAuthenticatedArg = mainViewModel.isAuthenticated
                    transitionNameArg = transitionName
                }.also { action ->
                    sharedView?.let {
                        val extras = FragmentNavigatorExtras(it to transitionName)
                        findNavController().navigate(action, extras)
                    } ?: run {
                        findNavController().navigate(action)
                    }
                }
        }
    }

    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }

    private val homeEpoxyController: HomeEpoxyController by inject {
        parametersOf(callbacks, glideRequestManager)
    }

    private val onDestroyView: PublishRelay<Any> = PublishRelay.create()

    override val initialState: UIState by lazy {
        UIState.HomeScreenState(
            popularMoviesResource = Resource.Loading(),
            topRatedMoviesResource = Resource.Loading(),
            searchResultsResource = null
        )
    }

    private val homeViewModel: HomeViewModel by viewModel {
        parametersOf(initialState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle(getString(R.string.app_name))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel.apply {

            message.observe(viewLifecycleOwner, Observer { message ->
                view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
            })

            state.observe(viewLifecycleOwner, Observer { state ->
                renderState(state)
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

        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition.apply {
            duration = 500
        }

        sharedElementReturnTransition = transition.apply {
            duration = 500
        }

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

    override fun onBackPressed(): Boolean {
        return with(homeViewModel) {
            state.value!!.searchResultsResource?.let {
                clearSearchResults()
                searchBox.etSearchBox.setText("")
                this@HomeFragment.view?.requestFocus()
                false
            } ?: true
        }
    }

    override fun renderState(state: UIState.HomeScreenState) {
        homeEpoxyController.setData(state)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyView.accept(Notification.INSTANCE)
        mainViewModel.setBackPressListener(null)
    }
}
