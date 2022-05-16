package com.mk.match

import androidx.room.Embedded
import androidx.room.Relation
import com.mk.competitors.CompetitorEntity

data class MatcheWithPlayers(
    @Embedded val match: MatchEntity,
    @Relation(parentColumn = MatchEntity.COMPETITOR_1_ID,
    entityColumn = "id")
    val player1: CompetitorEntity,
    @Relation(parentColumn = MatchEntity.COMPETITOR_2_ID,
        entityColumn = "id")
    val player2: CompetitorEntity,
)