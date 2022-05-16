package com.mk.competitors.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mk.competitors.data.CompetitorEntity.Companion.COMPETITOR_NAME

@Entity(tableName = CompetitorEntity.TABLE_NAME,
indices = [Index(value = [COMPETITOR_NAME], unique = true)])
data class CompetitorEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo()val name: String
    ) {
    companion object {
        const val TABLE_NAME = "competitors"
        const val COMPETITOR_NAME = "name"
        const val ID = "id"
    }
}