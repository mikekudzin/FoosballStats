package com.mk.match

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mk.base.BaseRxViewModel
import com.mk.competitors.CompetitorsDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class AddMatchViewModel @Inject constructor(
    private val competitorsDAO: CompetitorsDAO,
    private val matchesDAO: MatchesDAO,
    @ApplicationContext val context: Context
) : BaseRxViewModel() {

    class Match {
        var player1: UIPlayer = UIPlayer.NoPlayer
        var player2: UIPlayer = UIPlayer.NoPlayer
        var score1: Int = -1
        var score2: Int = -1

//        fun toMatchEntity() : MatchEntity {
//            assert(player1 is UIPlayer.SelectedPlayer)
//            assert(player2 is UIPlayer.SelectedPlayer)
//            assert(score1 > 0 && score2 > 0)
//            return MatchEntity(competitor1Id = player1.)
//        }
    }

//  sealed class

    private var match = Match()
    val matchData = MutableLiveData<Match>(match)

    private val player1Subject = BehaviorSubject.create<UIPlayer>()
    private val player2Subject = BehaviorSubject.create<UIPlayer>()
    private val player1ScoreSubject = BehaviorSubject.create<String>()
    private val player2ScoreSubject = BehaviorSubject.create<String>()

    private val _player1Suggestions: MutableLiveData<List<UIPlayer.SelectedPlayer>> =
        MutableLiveData()
    val player1Suggestions: LiveData<List<UIPlayer.SelectedPlayer>>
        get() = _player1Suggestions

    private val _player2Suggestions: MutableLiveData<List<UIPlayer.SelectedPlayer>> =
        MutableLiveData()
    val player2Suggestions: LiveData<List<UIPlayer.SelectedPlayer>>
        get() = _player2Suggestions

    init {
        initPlayer1Selection()
        initPlayer2Selection()
        player1Subject.onNext(UIPlayer.NoPlayer)
        player2Subject.onNext(UIPlayer.NoPlayer)
    }


    private fun initPlayer1Selection() {
        val subscription = player1Subject
            .switchMap { player ->
                filterPlayerOut(player)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _player2Suggestions.value = it }

        bindDisposable(subscription)
    }

    private fun initPlayer2Selection() {
        val subscription = player2Subject
            .switchMap { player ->
                filterPlayerOut(player)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _player1Suggestions.value = it
            }

        bindDisposable(subscription)
    }

    private fun filterPlayerOut(player: UIPlayer) =
        competitorsDAO
            .getCompetitors()
            .toObservable()
            .flatMapSingle { competitors ->
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

    fun player1Selected(player: UIPlayer) {
        match.player1 = player
        matchData.value = match
        player1Subject.onNext(player)
    }

    fun player2Selected(player: UIPlayer) {
        match.player2 = player
        matchData.value = match
        player2Subject.onNext(player)
    }

    fun player1ScoreSet(value: String) {
        player1ScoreSubject.onNext(value)
    }

    fun player2ScoreSet(value: String) {
        player2ScoreSubject.onNext(value)
    }

    fun scoreFilled(score1: Int, score2: Int) {

    }

//    fun initVerifyFormFilled() {
//        Observable.combineLatest(player1Subject, player2Subject, )
//    }


    fun saveMatchSTUB(score1: String, score2: String) {

        val player1 = match.player1
        val player2 = match.player2
        var score1Int = 0
        var score2Int = 0
        try {
            score1Int = Integer.parseInt(score1)
            score2Int = Integer.parseInt(score2)

        } catch (e: NumberFormatException) {
            Log.d("!!!!ERR" ,"$e")
        }
        match.score1 = score1Int
        match.score2 = score2Int


        if (player1 is UIPlayer.SelectedPlayer && player2 is UIPlayer.SelectedPlayer) {
            val entity = MatchEntity(
                startTime = System.currentTimeMillis() - DEFAULT_LENGTH_MILLIS,
                matchTime = DEFAULT_LENGTH_MILLIS,
                competitor1Id = player1.id,
                competitor2Id = player2.id,
                competitor1Score = score1Int,
                competitor2Score = score2Int
            )
            val disposable = matchesDAO.saveMatch(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { Toast.makeText(context, "Match saved", Toast.LENGTH_LONG).show() }
            bindDisposable(disposable)
        }
    }
}

const val DEFAULT_LENGTH_MILLIS = 10 * 60 * 1000L //10 min
