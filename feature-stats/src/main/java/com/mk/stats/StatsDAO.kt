package com.mk.stats

import androidx.room.Dao
import androidx.room.Query
import com.mk.competitors.CompetitorEntity
import com.mk.match.MatchEntity
import io.reactivex.rxjava3.core.Flowable

@Dao
interface StatsDAO {

//    @Query("SELECT *, c1.name AS competitor1Name, c2.name AS competitor2Name FROM ${MatchEntity.MATCH_TABLE_NAME} " +
//            "INNER JOIN ${CompetitorEntity.TABLE_NAME} AS c1 ON competitor1Id = c1.id " +
//            "INNER JOIN ${CompetitorEntity.TABLE_NAME} AS c2 ON competitor2Id = c2.id;")
//    fun getAllMatches(): Flowable<List<MatchEntity>>

    // Количество матчей
//    @Query("SELECT ${CompetitorEntity.ID} AS player_id, competitors.name AS playerName, count() AS played_total FROM competitors " +
//            "JOIN matches ON (${CompetitorEntity.ID} = ${MatchEntity.COMPETITOR_1_ID} OR ${CompetitorEntity.ID} = ${MatchEntity.COMPETITOR_2_ID}) GROUP BY ${CompetitorEntity.ID};")

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