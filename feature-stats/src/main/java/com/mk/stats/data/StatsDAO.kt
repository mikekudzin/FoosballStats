package com.mk.stats.data

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable

@Dao
interface StatsDAO {

    @Query("SELECT * FROM player_stats;")
    fun getPlayersStats(): Flowable<List<PlayerStats>>

}