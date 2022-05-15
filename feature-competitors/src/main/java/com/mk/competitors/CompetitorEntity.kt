package com.mk.competitors

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CompetitorEntity.TABLE_NAME)
data class CompetitorEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String
    ) {
    companion object {
        const val TABLE_NAME = "competitors"
        const val COMPETITOR_NAME = "name"
        const val ID = "id"
    }
}