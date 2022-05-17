package com.mk.match.data

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface MatchesDAO : MatchesRepository{

    @Insert
    override fun saveMatch(match: MatchEntity): Completable

    @Update
    override fun updateMatch(match: MatchEntity): Completable

    @Transaction
    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    override fun getMatch(matchId: Int): Maybe<MatchWithPlayers>

    @Transaction
    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} ORDER BY recordTime DESC")
    override fun getAllMatches(): Flowable<List<MatchWithPlayers>>

    @Query("DELETE FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    override fun deleteMatch(matchId: Int) : Completable

}