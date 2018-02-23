package github.com.rhacco.dotascoop.sources.databases.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import io.reactivex.Single

@Dao
interface HeroesDao {
    @Query("SELECT * FROM heroes")
    fun getHeroes(): Single<List<HeroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeHeroes(heroes: List<HeroEntity>)
}