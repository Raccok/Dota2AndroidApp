package github.com.raccok.dota2androidapp.sources.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import github.com.raccok.dota2androidapp.entities.HeroEntity
import github.com.raccok.dota2androidapp.sources.db.dao.HeroesDao

@Database(entities = arrayOf(HeroEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reposDao(): HeroesDao

    companion object {
        const val DATABASE_NAME = "dota-app-db"
    }
}