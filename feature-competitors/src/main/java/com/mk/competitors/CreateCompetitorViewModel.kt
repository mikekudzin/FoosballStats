package com.mk.competitors

import android.content.Context
import android.widget.Toast
import com.mk.base.BaseRxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateCompetitorViewModel @Inject constructor(
    private val dao: CompetitorsDAO,
    @ApplicationContext private val context: Context
) : BaseRxViewModel() {

    fun createCompetitor(name: String) {
        val disposable =
            dao.addCompetitor(CompetitorEntity(name = name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show() }
        bindDisposables(disposable)
    }

}