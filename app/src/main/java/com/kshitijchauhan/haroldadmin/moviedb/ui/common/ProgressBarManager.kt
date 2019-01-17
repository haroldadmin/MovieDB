package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import com.jakewharton.rxrelay2.PublishRelay
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.ProgressBarNotification
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import java.util.*

/**
 * This class is used to manage whether the progress bar should visible or not.
 *
 * Each task represents a network operation which might take a long time. While this task is running, we need the
 * progress bar to be visible to the user. Each such task is added to a tasks queue, and whenever a task is created
 * or completed, we send a notification to hide/show the progress bar.
 */
class ProgressBarManager {

    private val loadingQueue = ArrayDeque<LoadingTask>()
    private val progressBarRelay: PublishRelay<ProgressBarNotification> = PublishRelay.create()

    fun addTask(task: LoadingTask) {
        loadingQueue.add(task)
        updateProgressBar()
    }

    fun completeTaskByTag(tag: String) {
        val task = loadingQueue.asIterable().firstOrNull { it.tag == tag }
        task?.let { completeTask(it) }
    }

    fun completeTask(task: LoadingTask) {
        loadingQueue.removeFirstOccurrence(task)
        updateProgressBar()
    }

    fun findTaskByTag(tag: String) = loadingQueue.firstOrNull { it.tag == tag }

    private fun updateProgressBar() {
        if (loadingQueue.isEmpty()) {
            progressBarRelay.accept(ProgressBarNotification(false))
        } else {
            progressBarRelay.accept(ProgressBarNotification(true))
        }
    }

    fun getProgressBarRelay() = this.progressBarRelay
}