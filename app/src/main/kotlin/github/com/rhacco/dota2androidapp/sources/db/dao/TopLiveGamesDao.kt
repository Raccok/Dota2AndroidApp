package github.com.rhacco.dota2androidapp.sources.db.dao

import android.arch.persistence.room.*
import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import io.reactivex.Flowable

@Dao
interface TopLiveGamesDao {
    @Query("SELECT * FROM game_list")
    fun loadAllTopLiveGames(): Flowable<List<TopLiveGameEntity>>

    @Update
    fun updateTopLiveGames(products: MutableList<TopLiveGameEntity>)
}
