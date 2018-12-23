package com.kshitijchauhan.haroldadmin.moviedb.ui.discover


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide

import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : BaseFragment() {

    override val associatedState: UIState = UIState.DiscoverScreenState

    private lateinit var mainViewModel: MainViewModel
    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var moviesAdapter: MoviesAdapter

    companion object {
        fun newInstance() = DiscoverFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        discoverViewModel = ViewModelProviders.of(this).get(DiscoverViewModel::class.java)
        discoverViewModel.moviesUpdate.observe(viewLifecycleOwner, Observer(moviesAdapter::updateList))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridLayoutManager = GridLayoutManager(context, 2)
        moviesAdapter = MoviesAdapter(Glide.with(this), mutableListOf())
        rvMovies.apply {
            layoutManager = gridLayoutManager
            adapter = moviesAdapter
        }
    }

    override fun onDestroyView() {
        val lastState: UIState? = mainViewModel.peekState()?.first
        if (isRemoving && lastState != associatedState) {
            lastState?.let {
                mainViewModel.updateState(associatedState to lastState)
            }
        }
        super.onDestroyView()
    }
}
