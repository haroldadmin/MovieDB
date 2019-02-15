package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.ProgressBarNotification

/**
 * This class is used to manage whether the progress bar should visible or not.
 *
 * Each task represents a network operation which might take a long time. While this task is running, we need the
 * progress bar to be visible to the user. Each such task is added to a tasks queue, and whenever a task is created
 * or completed, we send a notification to hide/show the progress bar. The status of the progress bar is exposed through
 * an immutable live data object.
 *
 * This class is aware of the lifecycle of objects that add loading tasks to it. When such an object is destroyed before
 * its added tasks are completed, they are cleared automatically.
 */
class ProgressBarManager : DefaultLifecycleObserver {

    private val progressBarLiveData = MutableLiveData<ProgressBarNotification>()
    private val tasksRegistry = mutableMapOf<LifecycleOwner, MutableList<LoadingTask>>()

    init {
        progressBarLiveData.postValue(ProgressBarNotification(visible = false))
    }

    /**
     * This function adds the newly submitted task to the task registry. If the tasks registry map already has an entry
     * for this task's lifecycle owner, then it is added to its task list. Otherwise, a new list if created for its
     * lifecycle owner. Whenever a task is added, the progress bar is updated.
     *
     * Also registers this class as an observer of the lifecycle of the associated lifecycle owner of the given task.
     *
     * @param task The newly added task to be added to the registry
     */
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

    /**
     * This is a convenience method to find a task by its associated tag, and if it exists and remove it from the tasks
     * registry.
     *
     * @param tag The tag associated with the task
     */
    fun completeTaskByTag(tag: String, lifecycleOwner: LifecycleOwner) {
        findTaskByTag(tag, lifecycleOwner)?.let {
            completeTask(it)
        }
    }

    /**
     * Removes the given task object from the lifecycle registry, and updates the progress bar status after it.
     *
     * @param task The task to be removed/completed
     */
    fun completeTask(task: LoadingTask) {
        tasksRegistry[task.lifecycleOwner]?.remove(task)
        updateProgressBar()
    }

    /**
     * Find a task in the registry using its associated tag
     *
     * @param tag Tag associated with the task
     * @param lifecycleOwner The lifecycle owner who submitted this task
     *
     * @return The requested loading task, if it is found
     */
    fun findTaskByTag(tag: String, lifecycleOwner: LifecycleOwner): LoadingTask? {
        return tasksRegistry[lifecycleOwner]?.firstOrNull { task ->
            task.tag == tag
        }
    }

    /**
     * Checks if the task registry has no tasks in it. If true, hides the progress bar. Else, shows it.
     */
    private fun updateProgressBar() {
        if (tasksRegistry.values.all { list -> list.isEmpty() }) {
            progressBarLiveData.postValue(ProgressBarNotification(false))
        } else {
            progressBarLiveData.postValue(ProgressBarNotification(true))
        }
    }

    /**
     * Get the live data object for listening to progress bar updates.
     *
     * @return immutable form of [progressBarLiveData]
     */
    fun getProgressBarLiveData(): LiveData<ProgressBarNotification> = this.progressBarLiveData

    /**
     * Invoked whenever the lifecycle owner of an associated task is destroyed.
     * It removes all the associated tasks of this owner, in case they were not complete yet.
     */
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        tasksRegistry.remove(owner)
        updateProgressBar()
    }
}