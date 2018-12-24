package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import io.reactivex.android.plugins.RxAndroidPlugins
import kotlinx.android.synthetic.main.fragment_search.*
import com.jakewharton.rxbinding3.*

class SearchFragment : BaseFragment() {

    override val associatedState: UIState = UIState.SearchScreenState

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchAdapter: SearchResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        searchViewModel.searchUpdate.observe(viewLifecycleOwner, Observer {
            searchAdapter.updateList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchResultsAdapter(emptyList())
        rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }
}
