package com.mk.foosballmatch.matches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mk.foosballmatch.competitors.CompetitorEntity

@Dao
interface MatchesDAO {

    @Insert
    fun saveMatch(match: MatchEntity)

    @Query("SELECT *, c1.name AS competitor1Name, c2.name AS competitor2Name FROM ${MatchEntity.MATCH_TABLE_NAME} " +
            "INNER JOIN ${CompetitorEntity.TABLE_NAME} AS c1 ON competitor1Id = c1.id " +
            "INNER JOIN ${CompetitorEntity.TABLE_NAME} AS c2 ON competitor2Id = c2.id")
    fun getAllMatches(): List<MatchEntity>


}