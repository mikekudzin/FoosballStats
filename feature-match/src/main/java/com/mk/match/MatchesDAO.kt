package com.mk.match

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MatchesDAO {

    @Insert
    fun saveMatch(match: MatchEntity): Completable

    @Query("SELECT * FROM ${MatchEntity.MATCH_TABLE_NAME}")
    fun getAllMatches(): Flowable<List<MatchEntity>>


}