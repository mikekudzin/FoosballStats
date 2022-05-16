package com.mk.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

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