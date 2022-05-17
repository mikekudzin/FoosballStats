package com.mk.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseRxViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun bindDisposables(vararg disposable: Disposable) {
        compositeDisposable.addAll(*disposable)
    }

    fun withBoundSubscription(block: () -> Disposable) {
        bindDisposables(block())
    }
}