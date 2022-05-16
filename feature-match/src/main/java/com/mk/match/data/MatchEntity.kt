package com.mk.match.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mk.competitors.data.CompetitorEntity
import com.mk.match.data.MatchEntity.Companion.COMPETITOR_1_ID
import com.mk.match.data.MatchEntity.Companion.MATCH_TABLE_NAME

@Entity(
    tableName = MATCH_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = CompetitorEntity::class,
        parentColumns = arrayOf(CompetitorEntity.ID),
        childColumns = arrayOf(COMPETITOR_1_ID),
        onDelete = ForeignKey.CASCADE
    )]
)
open class MatchEntity(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val recordTime: Long = 0,
    @ColumnInfo(name = COMPETITOR_1_ID) val competitor1Id: Int,
    @ColumnInfo(name = COMPETITOR_2_ID) val competitor2Id: Int,
    val competitor1Score: Int,
    val competitor2Score: Int,
) {
    companion object {
        const val MATCH_TABLE_NAME = "matches"
        const val COMPETITOR_1_ID = "competitor1Id"
        const val COMPETITOR_2_ID = "competitor2Id"
    }
}
