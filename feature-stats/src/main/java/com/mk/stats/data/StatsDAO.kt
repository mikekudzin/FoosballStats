package com.mk.stats.data

import androidx.room.Dao
import androidx.room.Query
import com.mk.competitors.data.CompetitorEntity
import com.mk.match.data.MatchEntity
import com.mk.stats.MatchesPlayed
import com.mk.stats.PlayerStats
import io.reactivex.rxjava3.core.Flowable

@Dao
interface StatsDAO {

    @Query("SELECT * FROM matches_played;")
    fun getMatchesPlayed(): Flowable<List<MatchesPlayed>>

    @Query("SELECT * FROM player_stats;")
    fun getPlayersStats(): Flowable<List<PlayerStats>>

    @Query("SELECT ${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.ID} AS playerId, " +
            "${CompetitorEntity.COMPETITOR_NAME} AS playerName, count(matches.id) AS matchesPlayed " +
            "FROM ${CompetitorEntity.TABLE_NAME} " +
            "LEFT JOIN ${MatchEntity.MATCH_TABLE_NAME} ON (${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.ID} = ${MatchEntity.COMPETITOR_1_ID} " +
            "OR ${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.ID} = ${MatchEntity.COMPETITOR_2_ID}) " +
            "GROUP BY playerId ORDER BY matchesPlayed DESC;")
    fun getMatchesPlayedT(): Flowable<List<MatchesPlayed>>

}