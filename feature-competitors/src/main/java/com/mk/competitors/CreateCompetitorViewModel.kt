package com.mk.competitors

import androidx.lifecycle.LiveData
import com.mk.base.BaseRxViewModel
import com.mk.base.SingleLiveEvent
import com.mk.base.UiEvent
import com.mk.competitors.data.CompetitorEntity
import com.mk.competitors.data.CompetitorsDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateCompetitorViewModel @Inject constructor(
    private val dao: CompetitorsDAO,
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