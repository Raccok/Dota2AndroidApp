package github.com.rhacco.dota2androidapp.sources.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import github.com.rhacco.dota2androidapp.sources.db.dao.HeroesDao
import github.com.rhacco.dota2androidapp.sources.db.dao.ProPlayersDao

@Database(entities = arrayOf(HeroEntity::class, ProPlayerEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heroesDao(): HeroesDao
    abstract fun proPlayersDao(): ProPlayersDao

    companion object {
        const val DATABASE_NAME = "dota-app-db"
    }
}