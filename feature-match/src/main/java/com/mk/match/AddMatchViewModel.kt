package com.mk.match

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mk.base.BaseRxViewModel
import com.mk.base.SingleLiveEvent
import com.mk.base.UiEvent
import com.mk.competitors.data.CompetitorsRepository
import com.mk.match.data.MatchEntity
import com.mk.match.data.MatchWithPlayers
import com.mk.match.data.MatchesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AddMatchViewModel @AssistedInject constructor(
    private val competitorsRepository: CompetitorsRepository,
    private val matchesRepository: MatchesRepository,
    @Assisted val matchId: Int
) : BaseRxViewModel() {

    private var match = Match()
    val matchUI = MutableLiveData<UIState>()

    private val player1Subject = BehaviorSubject.create<UIPlayer>()
    private val player2Subject = BehaviorSubject.create<UIPlayer>()
    private val matchDataSubject = BehaviorSubject.create<Match>()

    private val _player1Suggestions: MutableLiveData<List<UIPlayer.SelectedPlayer>> =
        MutableLiveData()
    val player1Suggestions: LiveData<List<UIPlayer.SelectedPlayer>>
        get() = _player1Suggestions

    private val _player2Suggestions: MutableLiveData<List<UIPlayer.SelectedPlayer>> =
        MutableLiveData()
    val player2Suggestions: LiveData<List<UIPlayer.SelectedPlayer>>
        get() = _player2Suggestions

    private val _uiEvent = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent>
        get() = _uiEvent

    init {
        initPlayer1Selection()
        initPlayer2Selection()
        initVerification()
        setMatchIdForEdit(matchId)
        player1Subject.onNext(UIPlayer.NoPlayer)
        player2Subject.onNext(UIPlayer.NoPlayer)
    }

    private fun initVerification() {
        withBoundSubscription {
            matchDataSubject.map { match ->
                with(match) {
                    var score1Int = -1
                    var score2Int = -1
                    try {
                        score1Int = Integer.parseInt(score1)
                        score2Int = Integer.parseInt(score2)
                    } catch (ex: Exception) {
                        // ignore
                    }

                    val matchDataValid =
                        player1 != UIPlayer.NoPlayer && player2 != UIPlayer.NoPlayer
                                && score1Int > -1 && score2Int > -1

                    UIState().apply {
                        this.matchData = match
                        this.dataValid = matchDataValid
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    matchUI.value = it
                }
        }
    }

    private fun setMatchIdForEdit(matchId: Int) {
        withBoundSubscription {
            matchesRepository.getMatch(matchId)
                .map { matchWithPlayers ->
                    Match.fromMatchEntity(matchWithPlayers)
                }
                .doOnSuccess { match ->
                    matchDataSubject.onNext(match)
                    player1Subject.onNext(match.player1)
                    player1Subject.onNext(match.player2)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    match = it
                }
        }
    }

    private fun initPlayer1Selection() {
        withBoundSubscription {
            player1Subject
                .switchMap { player ->
                    filterPlayerOut(player)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _player2Suggestions.value = it }
        }
    }

    private fun initPlayer2Selection() {
        withBoundSubscription {
            player2Subject
                .switchMap { player ->
                    filterPlayerOut(player)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _player1Suggestions.value = it
                }
        }
    }

    private fun filterPlayerOut(player: UIPlayer): Observable<MutableList<UIPlayer.SelectedPlayer>> {
        return competitorsRepository
            .getCompetitors()
            .toObservable()
            .switchMapSingle { competitors ->
                Observable.fromIterable(competitors).filter { competitor ->
                    when (player) {
                        is UIPlayer.SelectedPlayer -> {
                            player.id != competitor.id
                        }
                        else -> true
                    }
                }.map { competitor ->
                    UIPlayer.SelectedPlayer(competitor.id, competitor.name)
                }.toList()
            }
    }

    fun player1Selected(player: UIPlayer) {
        match.player1 = player
        matchDataSubject.onNext(match)
        player1Subject.onNext(player)
    }

    fun player2Selected(player: UIPlayer) {
        match.player2 = player
        matchDataSubject.onNext(match)
        player2Subject.onNext(player)
    }

    fun player1ScoreSet(value: String) {
        if (match.score1 == value) {
            return
        }

        match.score1 = value
        matchDataSubject.onNext(match)
    }

    fun player2ScoreSet(value: String) {
        if (match.score2 == value) {
            return
        }

        match.score2 = value
        matchDataSubject.onNext(match)
    }

    fun saveMatch() {
        val player1 = match.player1
        val player2 = match.player2
        var score1Int = -1
        var score2Int = -1
        try {
            score1Int = Integer.parseInt(match.score1)
            score2Int = Integer.parseInt(match.score2)
        } catch (ex: Exception) {
            // ignore
        }

        if (player1 is UIPlayer.SelectedPlayer && player2 is UIPlayer.SelectedPlayer) {
            val entity = MatchEntity(
                id = match.id,
                recordTime = match.matchTime,
                competitor1Id = player1.id,
                competitor2Id = player2.id,
                competitor1Score = score1Int,
                competitor2Score = score2Int
            )
            withBoundSubscription {
                Observable.just(match).flatMapCompletable {
                    if (match.id > 0) {
                        matchesRepository.updateMatch(entity)
                    } else {
                        matchesRepository.saveMatch(entity)
                    }
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { _uiEvent.setValue(UiEvent.Success) }
            }
        }
    }
}

class UIState {
    var matchData = Match()
    var dataValid = false
}

class Match {
    var id = 0
    var matchTime = Calendar.getInstance().timeInMillis
    var player1: UIPlayer = UIPlayer.NoPlayer
    var player2: UIPlayer = UIPlayer.NoPlayer
    var score1: String = ""
    var score2: String = ""

    companion object {
        fun fromMatchEntity(matchWithPlayers: MatchWithPlayers): Match {
            return Match().apply {
                id = matchWithPlayers.match.id
                matchTime = matchWithPlayers.match.recordTime
                player1 = UIPlayer.SelectedPlayer(
                    matchWithPlayers.player1.id,
                    matchWithPlayers.player1.name
                )
                player2 = UIPlayer.SelectedPlayer(
                    matchWithPlayers.player2.id,
                    matchWithPlayers.player2.name
                )
                score1 = matchWithPlayers.match.competitor1Score.toString()
                score2 = matchWithPlayers.match.competitor2Score.toString()
            }
        }
    }
}
