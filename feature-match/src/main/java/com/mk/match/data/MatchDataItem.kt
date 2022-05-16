package com.mk.match.data

import androidx.room.Embedded
import androidx.room.Relation
import com.mk.competitors.data.CompetitorEntity

data class MatchWithPlayers(
    @Embedded val match: MatchEntity,
    @Relation(parentColumn = MatchEntity.COMPETITOR_1_ID,
    entityColumn = "id")
    val player1: CompetitorEntity,
    @Relation(parentColumn = MatchEntity.COMPETITOR_2_ID,
        entityColumn = "id")
    val player2: CompetitorEntity,
)