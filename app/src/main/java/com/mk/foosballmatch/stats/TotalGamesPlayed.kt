package com.mk.foosballmatch.stats

import androidx.room.DatabaseView
import com.mk.foosballmatch.competitors.CompetitorEntity

@DatabaseView("SELECT ${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.ID} AS ${TotalGamesPlayed.COMPETITOR_ID}," +
        "${CompetitorEntity.TABLE_NAME}.${CompetitorEntity.COMPETITOR_NAME} AS ${TotalGamesPlayed.COMPETITOR_NAME}")
data class TotalGamesPlayed(
    val competitorId: Int,
    val competitorName: String,
    val gamesPlayed: Int
) {
    companion object {
        const val COMPETITOR_ID = "competitorId"
        const val COMPETITOR_NAME = "competitorName"
    }
}