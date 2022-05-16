package com.mk.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseRxViewModel: ViewModel() {
    protected val vmScopeDisposable = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
    }

    fun bindDisposables(vararg disposable: Disposable) {
        vmScopeDisposable.addAll(*disposable)
    }
}