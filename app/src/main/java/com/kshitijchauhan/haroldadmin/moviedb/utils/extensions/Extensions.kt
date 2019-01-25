package com.kshitijchauhan.haroldadmin.moviedb.utils.extensions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.Config
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Any.log(message: String) {
     Log.d(this::class.java.simpleName, message)
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

fun String?.getPosterUrl(): String {
    return this?.let {
        "${Config.BASE_IMAGE_URL}${Config.DEFAULT_POSTER_SIZE}$it"
    } ?: ""
}

fun String?.getBackdropUrl(): String {
    return this?.let {
        "${Config.BASE_IMAGE_URL}${Config.DEFAULT_BACKDROP_SIZE}${this}"
    } ?: ""
}

fun String?.getProfilePictureUrl(): String {
    return this?.let {
        "${Config.BASE_IMAGE_URL}${Config.SMALL_PROFILE_PICTURE_SIZE}$this"
    } ?: ""
}

fun Float.getNumberOfColumns(context: Context): Int {
    val screenWidth = context.resources.displayMetrics.widthPixels
    return screenWidth.div(this).toInt()
}

fun String.toYoutubeUrl() = "https://www.youtube.com/watch?v=$this"

fun <T> List<T>.firstOrDefault(default: T): T {
    return this.firstOrNull() ?: default
}