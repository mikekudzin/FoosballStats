package com.mk.competitors.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface CompetitorsRepository {

    fun getCompetitors(): Flowable<List<CompetitorEntity>>

    fun addCompetitor(competitor: CompetitorEntity): Completable
}