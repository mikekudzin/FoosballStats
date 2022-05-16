package com.mk.match

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MatchesDAO {

    @Insert
    fun saveMatch(match: MatchEntity): Completable

    @Update()
    fun updateMatch(match: MatchEntity): Completable

    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} ORDER BY recordTime DESC")
    fun getAllMatches(): Flowable<List<MatcheWithPlayers>>

    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    fun getMatch(matchId: Int): Flowable<MatcheWithPlayers>

    @Query("DELETE FROM ${MatchEntity.MATCH_TABLE_NAME} WHERE id = :matchId")
    fun deleteMatch(matchId: Int) : Completable

}