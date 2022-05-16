package com.mk.stats

import androidx.room.DatabaseView
import com.mk.competitors.data.CompetitorEntity
import com.mk.match.data.MatchEntity

@DatabaseView(viewName = "matches_played",
    value = "SELECT ${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.ID} AS playerId, ${CompetitorEntity.COMPETITOR_NAME} AS playerName, count() AS matchesPlayed " +
        "FROM ${CompetitorEntity.TABLE_NAME} " +
        "JOIN ${MatchEntity.MATCH_TABLE_NAME} ON (${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.ID} = ${MatchEntity.COMPETITOR_1_ID} " +
        "OR ${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.ID} = ${MatchEntity.COMPETITOR_2_ID}) " +
        "GROUP BY playerId;")
data class MatchesPlayed(val playerId: Int, val playerName: String, val matchesPlayed: Int)


@DatabaseView(viewName = "player_stats",
value = "SELECT competitors.id AS playerId, competitors.name AS playerName, " +
        "SUM(CASE " +
        "WHEN (competitors.id = matches.competitor1Id AND matches.competitor1Score > matches.competitor2Score) THEN 1 " +
        "WHEN (competitors.id = matches.competitor2Id AND matches.competitor2Score > matches.competitor1Score) THEN 1 " +
        "ELSE 0 " +
        "END) AS wins, " +
        "SUM(CASE " +
        "WHEN (competitors.id = matches.competitor1Id AND matches.competitor1Score < matches.competitor2Score) THEN 1 " +
        "WHEN (competitors.id = matches.competitor2Id AND matches.competitor2Score < matches.competitor1Score) THEN 1 " +
        "ELSE 0 " +
        "END) AS loses, " +
        "SUM(CASE " +
        "WHEN (matches.competitor1Score = matches.competitor2Score) THEN 1 " +
        "ELSE 0 " +
        "END) as draws, " +
        "count(matches.id) AS totalMatches " +
        "FROM competitors LEFT JOIN matches ON (competitors.id = matches.competitor1Id OR competitors.id = matches.competitor2Id) GROUP BY playerId;")
data class PlayerStats(val playerId: Int, val playerName: String, val totalMatches: Int, val wins: Int, val loses: Int, val draws: Int)