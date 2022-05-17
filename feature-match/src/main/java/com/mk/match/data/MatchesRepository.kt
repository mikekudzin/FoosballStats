package com.mk.match.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

interface MatchesRepository {
    fun saveMatch(match: MatchEntity): Completable

    fun updateMatch(match: MatchEntity): Completable

    fun getAllMatches(): Flowable<List<MatchWithPlayers>>

    fun getMatch(matchId: Int): Maybe<MatchWithPlayers>

    fun deleteMatch(matchId: Int) : Completable
}