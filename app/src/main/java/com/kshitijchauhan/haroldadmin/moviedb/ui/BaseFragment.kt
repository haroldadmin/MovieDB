package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.log

abstract class BaseFragment: Fragment() {

    abstract val associatedState: UIState

    override fun onAttach(context: Context) {
        log("onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        log("onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        log("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        log("onStart")
        super.onStart()
    }

    override fun onResume() {
        log("onResume")
        super.onResume()
    }

    override fun onPause() {
        log("onPause")
        super.onPause()
    }

    override fun onStop() {
        log("onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        log("onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        log("onDetach")
        super.onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        log("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }
}