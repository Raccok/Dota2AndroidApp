package github.com.rhacco.dota2androidapp.sources.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import io.reactivex.Flowable

@Dao
interface HeroesDao {
    @Query("SELECT * FROM heroes")
    fun loadAllHeroes(): Flowable<List<HeroEntity>>

    @Query("SELECT * FROM heroes WHERE id IN (:heroIds)")
    fun getHeroesByIds(heroIds: List<Int>): Flowable<List<HeroEntity>>

    @Query("SELECT * FROM heroes WHERE localized_name = :heroLocalName LIMIT 1")
    fun getHeroByLocalName(heroLocalName: String): Flowable<List<HeroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<HeroEntity>)
}