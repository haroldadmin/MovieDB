package com.kshitijchauhan.haroldadmin.moviedb.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kshitijchauhan.haroldadmin.moviedb.BuildConfig
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun AppCompatActivity.app(): MovieDBApplication = this.application as MovieDBApplication

fun Any.log(message: String) {
    if (this !is BaseFragment) Log.d(this::class.java.simpleName, message)
}

/**
 * This method is used to replace the current fragment in the Activity
 * with the given new Fragment. It has a safeguard against adding the same
 * fragment twice: It does not perform the fragment transaction if the new type
 * of the new fragment is the same as the type of the already existing fragment.
 *
 * @param fragment The new fragment to display
 * @param containerId The ID of the container in which to place this fragment
 * @param addToBackStack To indicate whether this fragment should be added to
 * the backstack
 * @param backStackName The name to give when adding to backstack
 */
inline fun <reified T: BaseFragment> AppCompatActivity.replaceFragment(fragment: T, containerId: Int, addToBackStack: Boolean = true, backStackName: String? = null) {
    val currentFragment = this.supportFragmentManager
        .findFragmentById(containerId)

    if (currentFragment is T) {
        this.log("Not replacing the current fragment of type ${T::class.java.simpleName}")
        return
    }

    val ft = this.supportFragmentManager
        .beginTransaction()
        .replace(containerId, fragment)
    if (addToBackStack) {
        ft.addToBackStack(backStackName)
    }
    ft.commit()
}

fun Disposable.disposeWith(compositeDisposable: CompositeDisposable) = compositeDisposable.add(this)

val Unit?.safe get() = Unit