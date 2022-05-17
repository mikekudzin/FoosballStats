package com.mk.competitors.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CompetitorsDAO: CompetitorsRepository {

    @Query("SELECT * FROM ${CompetitorEntity.TABLE_NAME}")
    override fun getCompetitors(): Flowable<List<CompetitorEntity>>

    @Insert
    override fun addCompetitor(competitor: CompetitorEntity): Completable
}