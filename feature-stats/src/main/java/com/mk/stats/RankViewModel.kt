package com.mk.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mk.base.BaseRxViewModel
import com.mk.stats.data.PlayerStats
import com.mk.stats.data.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class RankViewModel @Inject constructor(private val statsRepository: StatsRepository) : BaseRxViewModel() {
    private val rankTypeSelected = BehaviorSubject.create<RankType>()
    private val _statResult = MutableLiveData<List<PlayerStatUI>>()
    val data: LiveData<List<PlayerStatUI>>
        get() = _statResult

    private val _rankType = MutableLiveData<RankType>()
    val rankType: LiveData<RankType>
        get() = _rankType

    init {
        initRankBasedOnInput()
        rankTypeSelected.onNext(RankType.MatchesPlayed)
    }

    private fun initRankBasedOnInput() {
        val disposable = Observable.combineLatest(
            statsRepository.getPlayersStats().toObservable(),
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
                }.toList()
                .map { playerStatList ->
                    playerStatList.mapIndexed { index, playerStats ->
                        PlayerStatUI(
                            index,
                            playerStats.playerId,
                            playerStats.playerName,
                            playerStats.statValue
                        )
                    }
                }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _statResult.value = it
            }

        val rankChange = rankTypeSelected
            .distinctUntilChanged()
            .subscribe { _rankType.value = it }

        bindDisposables(disposable, rankChange)
    }

    fun changeRankType(type: RankType) {
        rankTypeSelected.onNext(type)
    }
}

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