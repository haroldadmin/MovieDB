package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.snackbar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_details.*


class MovieDetailsFragment : BaseFragment() {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance(movieId: Int): MovieDetailsFragment {
            val newInstance = MovieDetailsFragment()
            newInstance.arguments = Bundle()
                .apply {
                    putInt(Constants.KEY_MOVIE_ID, movieId)
                }
            return newInstance
        }
    }

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
        arguments?.getInt(Constants.KEY_MOVIE_ID)?.let { id ->
            viewModel.getMovieDetails(id)
        }

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { movie ->
            updateView(movie)
        })
    }

    private fun updateView(movie: Movie) {
        Glide.with(this)
            .load(movie.posterPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .into(ivPoster)

        Glide.with(this)
            .asBitmap()
            .transition(BitmapTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .load(movie.backdropPath)
//            .listener(object : RequestListener<Bitmap> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Bitmap>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    view?.snackbar("Loading backdrop failed")
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Bitmap?,
//                    model: Any?,
//                    target: Target<Bitmap>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    Single
//                        .fromCallable {
//                            val palette = Palette.from(resource!!).generate()
//                            colourizeViews(palette)
//                        }
//                        .subscribeOn(Schedulers.computation())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe()
//                        .disposeWith(compositeDisposable)
//                    return false
//                }
//            })
            .into(ivBackdrop)

        tvTitle.text = movie.title
        chipMovieYear.text = movie.releaseDate
        chipMovieGenre.text = movie.genres[0].name
        chipMovieRating.text = String.format("%.2f", movie.voteAverage)
        tvDescription.text = movie.overview
    }

//    private fun colourizeViews(palette: Palette) {
//        val vibrant = palette.vibrantSwatch
//        palette.getDarkVibrantColor(ContextCompat.getColor(context!!, R.color.colorSurfaceDark))
//            .also {
//                detailsRootView.setBackgroundColor(it)
//            }
//        palette.getLightVibrantColor(ContextCompat.getColor(context!!, R.color.colorSurfaceDark))
//            .also {
//                (activity as AppCompatActivity).supportActionBar
//                    ?.apply {
//                        setBackgroundDrawable(ColorDrawable(it))
//                    }
//            }
//        vibrant?.rgb?.let {
//            detailsRootView.setBackgroundColor(it)
//        }
//    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

}
