package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.ProgressBarNotification

/**
 * This class is used to manage whether the progress bar should visible or not.
 *
 * Each task represents a network operation which might take a long time. While this task is running, we need the
 * progress bar to be visible to the user. Each such task is added to a tasks queue, and whenever a task is created
 * or completed, we send a notification to hide/show the progress bar.
 */
class ProgressBarManager : DefaultLifecycleObserver {

    private val progressBarRelay = MutableLiveData<ProgressBarNotification>()
    private val tasksRegistry = mutableMapOf<LifecycleOwner, MutableList<LoadingTask>>()

    fun addTask(task: LoadingTask) {
        with(task) {
            // If this owner already exists in our registry, add this task its task list else create a new list for it
            tasksRegistry[lifecycleOwner]?.add(this) ?: run {
                tasksRegistry[lifecycleOwner] = mutableListOf(this)
            }
            lifecycleOwner.lifecycle.addObserver(this@ProgressBarManager)
        }
        updateProgressBar()
    }

    fun completeTaskByTag(tag: String, lifecycleOwner: LifecycleOwner) {
        findTaskByTag(tag, lifecycleOwner)?.let {
            completeTask(it)
        }
    }

    fun completeTask(task: LoadingTask) {
        tasksRegistry[task.lifecycleOwner]?.remove(task)
        updateProgressBar()
    }

    fun findTaskByTag(tag: String, lifecycleOwner: LifecycleOwner): LoadingTask? {
        return tasksRegistry[lifecycleOwner]?.firstOrNull { task ->
            task.tag == tag
        }
    }

    private fun updateProgressBar() {
        if (tasksRegistry.values.all { list -> list.isEmpty() }) {
            progressBarRelay.postValue(ProgressBarNotification(false))
        } else {
            progressBarRelay.postValue(ProgressBarNotification(true))
        }
    }

    fun getProgressBarLiveData() = this.progressBarRelay

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        tasksRegistry.remove(owner)
        updateProgressBar()
    }
}