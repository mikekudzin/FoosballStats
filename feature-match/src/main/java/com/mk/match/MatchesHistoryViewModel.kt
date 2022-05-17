package com.mk.match

import android.icu.text.DateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mk.base.BaseRxViewModel
import com.mk.match.data.MatchesDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MatchesHistoryViewModel @Inject constructor(private val matchesDAO: MatchesDAO) :
    BaseRxViewModel() {

    data class MatchStats(
        val id: Int,
        val player1Name: String,
        val player1Score: String,
        val player2Name: String,
        val player2Score: String,
        val date: String
    )

    private val _matches = MutableLiveData<List<MatchStats>>(emptyList())
    val matches: LiveData<List<MatchStats>>
        get() = _matches

    init {
        withBoundSubscription {
         matchesDAO.getAllMatches()
            .switchMapSingle {
                Observable.fromIterable(it).map { matchWithPlayers ->
                    with(matchWithPlayers) {
                        val stringDate = DateFormat.getDateTimeInstance().format(match.recordTime);
                        MatchStats(
                            match.id,
                            player1.name,
                            match.competitor1Score.toString(),
                            player2.name,
                            match.competitor2Score.toString(),
                            stringDate,
                        )
                    }
                }.toList()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { matches ->
                _matches.value = matches
            }
        }
    }

    fun deleteMatch(matchId: Int) {
        withBoundSubscription {
            matchesDAO
                .deleteMatch(matchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {}
        }
    }
}