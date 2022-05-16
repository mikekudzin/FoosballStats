package com.mk.foosballmatch

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mk.competitors.data.CompetitorEntity
import com.mk.competitors.data.CompetitorsDAO
import com.mk.match.data.MatchEntity
import com.mk.match.data.MatchesDAO
import com.mk.stats.MatchesPlayed
import com.mk.stats.PlayerStats
import com.mk.stats.data.StatsDAO

@Database(entities = [CompetitorEntity::class, MatchEntity::class], views = [MatchesPlayed::class, PlayerStats::class], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun competitorsDao(): CompetitorsDAO
    abstract fun matchesDao(): MatchesDAO
    abstract fun statsDao(): StatsDAO
}