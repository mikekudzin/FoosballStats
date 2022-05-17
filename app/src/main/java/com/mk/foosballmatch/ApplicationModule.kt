package com.mk.foosballmatch

import android.content.Context
import androidx.room.Room
import com.mk.competitors.data.CompetitorsDAO
import com.mk.competitors.data.CompetitorsRepository
import com.mk.match.data.MatchesDAO
import com.mk.match.data.MatchesRepository
import com.mk.stats.data.StatsDAO
import com.mk.stats.data.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module(includes = [RepositoryBindings::class])
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context,
            DataBase::class.java, "foos.db"
        )
            // This shouldn't be kept for prod surely
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCompetitorsDao(dataBase: DataBase): CompetitorsDAO {
        return dataBase.competitorsDao()
    }

    @Provides
    @Singleton
    fun provideMatchesDao(dataBase: DataBase): MatchesDAO {
        return dataBase.matchesDao()
    }

    @Provides
    @Singleton
    fun provideStatsDao(dataBase: DataBase): StatsDAO {
        return dataBase.statsDao()
    }
}

@Module
@DisableInstallInCheck
interface RepositoryBindings {
    @Binds
    fun bindCompetitorsRepo(dao: CompetitorsDAO): CompetitorsRepository

    @Binds
    fun bindMatchesRepo(dao: MatchesDAO): MatchesRepository

    @Binds
    fun bindStatsRepo(dao: StatsDAO): StatsRepository
}