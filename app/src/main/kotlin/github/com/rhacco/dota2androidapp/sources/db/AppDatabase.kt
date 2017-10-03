package github.com.rhacco.dota2androidapp.sources.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import github.com.rhacco.dota2androidapp.sources.db.dao.HeroesDao
import github.com.rhacco.dota2androidapp.sources.db.dao.TopLiveGamesDao

@Database(entities = arrayOf(HeroEntity::class, TopLiveGameEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heroesDao(): HeroesDao
    abstract fun topLiveGamesDao(): TopLiveGamesDao

    companion object {
        const val DATABASE_NAME = "dota-app-db"
    }
}