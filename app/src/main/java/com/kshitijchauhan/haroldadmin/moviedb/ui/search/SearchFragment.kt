package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.jakewharton.rxbinding2.internal.Notification
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.hideKeyboard
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.view_searchbox.*
import kotlinx.android.synthetic.main.view_searchbox.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val onPauseRelay: PublishRelay<Any> = PublishRelay.create()

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val searchViewModel: SearchViewModel by viewModel()
    private val searchAdapter: SearchResultsAdapter by inject {
        parametersOf({ movieId: Int -> showDetails(movieId) })
    }

    override val associatedUIState: UIState = UIState.SearchScreenState

    override fun notifyBottomNavManager() {
        return
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle("Search")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel.searchResults.observe(viewLifecycleOwner, Observer {
            searchAdapter.submitList(it)
            if (it.isEmpty()) {
                TransitionManager.beginDelayedTransition(searchRootView)
                rvSearchResults.gone()
            } else {
                TransitionManager.beginDelayedTransition(searchRootView)
                rvSearchResults.visible()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateToolbarTitle()

        searchBox.etSearchBox.apply {
            requestFocus()
        }

        rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
            itemAnimator = SlideDownAlphaAnimator()
        }

        searchIcon.setOnClickListener {
            etSearchBox.clearFocus()
            searchRootView.requestFocus()
            activity?.hideKeyboard()
        }

        subscribeToQueries()
    }

    override fun onPause() {
        super.onPause()
        onPauseRelay.accept(Notification.INSTANCE)
    }

    private fun subscribeToQueries() {
        RxTextView.textChangeEvents(etSearchBox)
            .debounce(400, TimeUnit.MILLISECONDS)
            .map { event ->
                event.text().toString()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { query ->
                if (query.length > 2) {
                    searchViewModel.getMoviesForQuery(query)
                    TransitionManager.beginDelayedTransition(searchRootView)
                    rvSearchResults.visible()
                } else {
                    TransitionManager.beginDelayedTransition(searchRootView)
                    rvSearchResults.gone()
                }
            }
            .takeUntil(onPauseRelay)
            .subscribe()
    }

    private fun showDetails(movieId: Int) {
        mainViewModel.updateStateTo(UIState.DetailsScreenState(movieId))
    }
}
