package com.kshitijchauhan.haroldadmin.moviedb.utils.extensions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import com.kshitijchauhan.haroldadmin.moviedb.remote.Config
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Any.log(message: String) {
    if (this !is BaseFragment) Log.d(this::class.java.simpleName, message)
}

fun Disposable.disposeWith(compositeDisposable: CompositeDisposable) = compositeDisposable.add(this)

val Any?.safe get() = Unit

fun Handler.postDelayed(timeInMillis: Long, runnable: () -> Unit) {
    this.postDelayed({
        runnable.invoke()
    }, timeInMillis)
}

fun Context.getNumberOfColumns(itemWidth: Float): Int {
    val metrics = this.resources.displayMetrics
    val dpWidth = metrics.widthPixels / metrics.density
    return dpWidth.div(itemWidth).toInt()
}

fun Any?.getMainHandler() = Handler(Looper.getMainLooper())

fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}

fun GeneralMovieResponse.getPosterUrl(posterPath: String?): String {
    return posterPath?.let {
        "${Config.BASE_IMAGE_URL}${Config.SMALL_POSTER_SIZE}$it"
    } ?: ""
}

fun Float.getNumberOfColumns(context: Context): Int {
    val screenWidth = context.resources.displayMetrics.widthPixels
    return screenWidth.div(this).toInt()
}