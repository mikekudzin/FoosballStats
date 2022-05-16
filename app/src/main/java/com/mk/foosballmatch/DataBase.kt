package com.mk.foosballmatch

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mk.competitors.CompetitorEntity
import com.mk.competitors.CompetitorsDAO
import com.mk.match.MatchEntity
import com.mk.match.MatchesDAO
import com.mk.stats.MatchesPlayed
import com.mk.stats.PlayerStats
import com.mk.stats.StatsDAO

@Database(entities = [CompetitorEntity::class, MatchEntity::class], views = [MatchesPlayed::class, PlayerStats::class], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun competitorsDao(): CompetitorsDAO
    abstract fun matchesDao(): MatchesDAO
    abstract fun statsDao(): StatsDAO
}