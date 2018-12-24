package com.kshitijchauhan.haroldadmin.moviedb.utils

import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Transition
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun AppCompatActivity.app(): MovieDBApplication = this.application as MovieDBApplication

fun Any.log(message: String) {
    if (this !is BaseFragment) Log.d(this::class.java.simpleName, message)
}

/**
 * This method is used to replace the current nextFragment in the Activity
 * with the given new Fragment. It has a safeguard against adding the same
 * nextFragment twice: It does not perform the nextFragment transaction if the new type
 * of the new nextFragment is the same as the type of the already existing nextFragment.
 *
 * @param nextFragment The new nextFragment to display
 * @param containerId The ID of the container in which to place this nextFragment
 * @param addToBackStack To indicate whether this nextFragment should be added to
 * the backstack
 * @param backStackName The name to give when adding to backstack
 * @param enterTransition Enter transition for next fragment
 * @param exitTransition Exit transition for previous fragment
 * @param sharedElementTransition Shared element transition between the fragments
 * @param sharedElement Shared Element to be used for sharedElementTransition
 */
inline fun <reified T: BaseFragment> AppCompatActivity.replaceFragment(nextFragment: T, containerId: Int,
                                                                       addToBackStack: Boolean = true,
                                                                       backStackName: String? = null,
                                                                       enterTransition: Transition? = null,
                                                                       exitTransition: Transition? = null,
                                                                       sharedElementTransition: Transition? = null,
                                                                       sharedElement: View? = null) {
    val currentFragment = this.supportFragmentManager
        .findFragmentById(containerId)

    if (currentFragment is T) {
        this.log("Not replacing the current nextFragment of type ${T::class.java.simpleName}")
        return
    }

    exitTransition?.let { currentFragment?.exitTransition = it }
    enterTransition?.let { nextFragment.enterTransition = it }
    sharedElementTransition?.let {
        nextFragment.sharedElementEnterTransition = it
        nextFragment.sharedElementReturnTransition = it
    }

    val ft = this.supportFragmentManager
        .beginTransaction()
        .replace(containerId, nextFragment)
    if (addToBackStack) {
        ft.addToBackStack(backStackName)
    }

    sharedElement?.let { ft.addSharedElement(sharedElement, sharedElement.transitionName) }

    ft.commit()
}

fun Disposable.disposeWith(compositeDisposable: CompositeDisposable) = compositeDisposable.add(this)

val Unit?.safe get() = Unit

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Handler.postDelayed(timeInMillis: Long, runnable: () -> Unit) {
    this.postDelayed({
        runnable.invoke()
    }, timeInMillis)
}
