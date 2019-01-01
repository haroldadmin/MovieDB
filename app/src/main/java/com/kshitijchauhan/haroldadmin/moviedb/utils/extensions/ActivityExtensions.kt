package com.kshitijchauhan.haroldadmin.moviedb.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.transition.Transition
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment

fun AppCompatActivity.app(): MovieDBApplication = this.application as MovieDBApplication
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
inline fun <reified T : BaseFragment> AppCompatActivity.replaceFragment(
    nextFragment: T,
    containerId: Int,
    addToBackStack: Boolean = true,
    backStackName: String? = null,
    enterTransition: Transition? = null,
    exitTransition: Transition? = null,
    sharedElementTransition: Transition? = null,
    sharedElement: View? = null,
    transitionName: String? = null
) {
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

    if (transitionName == null) {
        sharedElement?.let { ft.addSharedElement(sharedElement, sharedElement.transitionName) }
    } else {
        sharedElement?.let { ft.addSharedElement(sharedElement, transitionName)}
    }

    ft.commit()
}

fun Activity.hideKeyboard() {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(
        if (this.currentFocus == null) {
            null
        } else {
            this.currentFocus?.windowToken
        }, InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun FragmentManager.clearBackStack() = repeat(this.backStackEntryCount) {
    this.popBackStack()
}