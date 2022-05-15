package com.mk.competitors

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CompetitorsDAO {

    @Query("SELECT * FROM ${CompetitorEntity.TABLE_NAME}")
    fun getCompetitors(): Flowable<List<CompetitorEntity>>

    @Insert
    fun addCompetitor(competitor: CompetitorEntity): Completable

}