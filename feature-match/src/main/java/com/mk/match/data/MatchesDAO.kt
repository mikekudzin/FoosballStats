package com.mk.match.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface MatchesDAO : MatchesRepository{

    @Insert
    override fun saveMatch(match: MatchEntity): Completable

    @Update
    override fun updateMatch(match: MatchEntity): Completable

    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} ORDER BY recordTime DESC")
    override fun getAllMatches(): Flowable<List<MatchWithPlayers>>

    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    override fun getMatch(matchId: Int): Maybe<MatchWithPlayers>

    @Query("DELETE FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    override fun deleteMatch(matchId: Int) : Completable

}