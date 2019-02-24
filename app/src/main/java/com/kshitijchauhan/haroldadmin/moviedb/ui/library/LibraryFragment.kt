package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.EqualSpaceGridItemDecoration
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getNumberOfColumns
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteView
import kotlinx.android.synthetic.main.fragment_library.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class LibraryFragment : BaseFragment(), MVRxLiteView<UIState.LibraryScreenState> {

    private val mainViewModel: MainViewModel by sharedViewModel()

    private val callbacks = object : EpoxyCallbacks {
        override fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?) {
            LibraryFragmentDirections.actionLibraryFragmentToMovieDetailsFragment()
                .apply {
                    movieIdArg = id
                    isAuthenticatedArg = mainViewModel.isAuthenticated
                    transitionNameArg = transitionName
                }
                .also { action ->
                    findNavController().navigate(action)
                }
        }
    }

    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }

    private val libraryEpoxyController: LibraryEpoxyController by inject {
        parametersOf(callbacks, glideRequestManager)
    }

    override val associatedUIState: UIState by lazy {
        UIState.LibraryScreenState(
            Resource.Loading(),
            Resource.Loading(),
            mainViewModel.isAuthenticated
        )
    }

    private val libraryViewModel: LibraryViewModel by viewModel {
        parametersOf(mainViewModel.accountId, associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle(getString(R.string.title_library_screen))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarTitle()
        rvLibrary.apply {
            val columns = resources.getDimension(R.dimen.movie_grid_poster_width).getNumberOfColumns(context!!)
            val space = resources.getDimension(R.dimen.movie_grid_item_space)
            layoutManager = GridLayoutManager(context, columns)
            addItemDecoration(EqualSpaceGridItemDecoration(space.roundToInt()))
            setController(libraryEpoxyController)
        }
        (view.parent as ViewGroup).doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        libraryViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            mainViewModel.showSnackbar(message)
        })

        if (mainViewModel.isAuthenticated) {
            libraryViewModel.apply {

                state.observe(viewLifecycleOwner, Observer { state ->
                    log("Received state update: $state")
                    renderState(state)
                })

                message.observe(viewLifecycleOwner, Observer { message ->
                    mainViewModel.showSnackbar(message)
                })

                forceRefreshCollection(mainViewModel.accountId, CollectionType.Favourite)
                forceRefreshCollection(mainViewModel.accountId, CollectionType.Watchlist)
            }
        }
    }

    override fun renderState(state: UIState.LibraryScreenState) {
        libraryEpoxyController.setData(state)
    }
}
