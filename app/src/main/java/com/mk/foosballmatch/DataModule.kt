package com.mk.foosballmatch

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.migration.AutoMigrationSpec
import com.mk.foosballmatch.competitors.CompetitorsDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DataBase {
        val db = Room.databaseBuilder(
            context,
            DataBase::class.java, "foos.db"
        )
                // This shouldn't be kept for prod surely
            .fallbackToDestructiveMigration()
            .build()
        return db
    }

    @Provides
    @Singleton
    fun provideCompetitorsDao(dataBase: DataBase): CompetitorsDAO {
        return dataBase.competitorsDao()
    }
}