package com.mk.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

/*
 * It is a pattern of how we can share data between a number of components (Fragment-Fragment,
 * Fragment-Activity) by using Jetpack lifecycle aware components.
 *
 * Although the notification use case is not perfect from the architectural point of view by making
 * modules fragments aware of some aspects of an activity implementation, it's main goal to demonstrate
 * the general approach.
 */
class SharedNotificationViewModel: ViewModel() {

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    private val _notification = SingleLiveEvent<String>()
    val notification: LiveData<String>
        get() = _notification

    fun showNotification(successText: String) {
        _notification.setValue(successText)
    }

    fun showNotificationError(errorText: String) {
        _error.setValue(errorText)
    }
}