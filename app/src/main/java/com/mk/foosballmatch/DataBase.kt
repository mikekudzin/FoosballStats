package com.mk.foosballmatch

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mk.foosballmatch.competitors.CompetitorEntity
import com.mk.foosballmatch.competitors.CompetitorsDAO
import com.mk.foosballmatch.matches.MatchEntity
import com.mk.foosballmatch.matches.MatchesDAO

@Database(entities = [CompetitorEntity::class, MatchEntity::class], version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun competitorsDao(): CompetitorsDAO
    abstract fun matchesDao(): MatchesDAO
}