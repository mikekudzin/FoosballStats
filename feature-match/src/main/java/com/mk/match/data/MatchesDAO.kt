package com.mk.match.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface MatchesDAO {

    @Insert
    fun saveMatch(match: MatchEntity): Completable

    @Update
    fun updateMatch(match: MatchEntity): Completable

    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} ORDER BY recordTime DESC")
    fun getAllMatches(): Flowable<List<MatchWithPlayers>>

    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    fun getMatch(matchId: Int): Maybe<MatchWithPlayers>

    @Query("DELETE FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    fun deleteMatch(matchId: Int) : Completable

}