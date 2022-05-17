package com.mk.stats.data

import io.reactivex.rxjava3.core.Flowable

interface StatsRepository {
    fun getPlayersStats(): Flowable<List<PlayerStats>>
}