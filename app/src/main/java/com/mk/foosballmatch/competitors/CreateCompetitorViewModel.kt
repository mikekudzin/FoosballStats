package com.mk.foosballmatch.competitors

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateCompetitorViewModel @Inject constructor(
    private val dao: CompetitorsDAO,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val vmScopeDisposable = CompositeDisposable()
    fun createCompetitor(name: String) {
        val disposable =
            dao.addCompetitor(CompetitorEntity(name = name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show() }
        vmScopeDisposable.addAll(disposable)
    }

    override fun onCleared() {
        super.onCleared()
    }
}