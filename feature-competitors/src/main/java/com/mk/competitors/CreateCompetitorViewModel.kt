package com.mk.competitors

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.mk.base.BaseRxViewModel
import com.mk.base.SingleLiveEvent
import com.mk.base.UiEvent
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

    private val _uiEvent = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent>
        get() = _uiEvent

    fun createCompetitor(name: String) {
        val disposable =
            dao.addCompetitor(CompetitorEntity(name = name.trim()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _uiEvent.setValue(UiEvent.Success) },
                    { err -> _uiEvent.setValue(UiEvent.Error(err.message?:"Failed to create")) }
                )
        bindDisposables(disposable)
    }

}