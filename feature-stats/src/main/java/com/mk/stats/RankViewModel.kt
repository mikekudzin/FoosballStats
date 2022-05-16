package com.mk.stats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mk.base.BaseRxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class RankViewModel @Inject constructor(private val statsDAO: StatsDAO) : BaseRxViewModel() {

    enum class RankType {
        MatchesPlayed,
        MatchesWon,
        TotalScore
    }

    data class PlayerStatUI(
        val position: Int = 0,
        val playerId: Int,
        val playerName: String,
        val statValue: Int
    )

    private val rankTypeSelected = BehaviorSubject.create<RankType>()
    private val _statResult = MutableLiveData<List<PlayerStatUI>>()
    val data: LiveData<List<PlayerStatUI>>
        get() = _statResult

    private val _rankType = MutableLiveData<RankType>()
    val rankType: LiveData<RankType>
        get() = _rankType

    init {
        val disposable = Observable.combineLatest(
            statsDAO.getPlayersStats().toObservable(),
            rankTypeSelected.distinctUntilChanged()
        ) { first: List<PlayerStats>, second: RankType ->
            first to second
        }.flatMapSingle { pair ->
            Observable
                .fromIterable(pair.first)
                .map { playerStats ->
                    val statValue = when (pair.second) {
                        RankType.MatchesPlayed -> playerStats.totalMatches
                        RankType.MatchesWon -> playerStats.wins
                        RankType.TotalScore -> playerStats.wins * 3 + playerStats.draws * 1
                    }
                    PlayerStatUI(
                        0,
                        playerStats.playerId,
                        playerStats.playerName,
                        statValue
                    )
                }
                .sorted { o1, o2 ->
                    o2.statValue.compareTo(o1.statValue)
//                    when (pair.second) {
//                        RankType.MatchesPlayed -> o2.totalMatches.compareTo(o1.totalMatches)
//                        RankType.MatchesWon -> o2.wins.compareTo(o1.wins)
//                    }
                }.toList()
                .map { playerStatList ->
                    playerStatList.mapIndexed { index, playerStats ->
//                        val statValue = when (pair.second) {
//                            RankType.MatchesPlayed -> playerStats.totalMatches
//                            RankType.MatchesWon -> playerStats.wins
//                        }
                        PlayerStatUI(
                            index,
                            playerStats.playerId,
                            playerStats.playerName,
                            playerStats.statValue
                        )
                    }
                }

//                .toSortedList { o1, o2 ->
//                    when (pair.second) {
//                        RankType.MatchesPlayed -> o2.totalMatches.compareTo(o1.totalMatches)
//                        RankType.MatchesWon -> o2.wins.compareTo(o1.wins)
//                    }
//                }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                it.forEach {
                    Log.d("!!!!TTT", "${it.playerId} ${it.playerName} ${it.statValue}")
                }
            }
            .subscribe {
                _statResult.value = it
            }

        val rankChange = rankTypeSelected
            .distinctUntilChanged()
            .subscribe { _rankType.value = it }


        bindDisposables(disposable, rankChange)
        rankTypeSelected.onNext(RankType.MatchesPlayed)
    }

    fun changeRankType(type: RankType) {
        rankTypeSelected.onNext(type)
    }

}