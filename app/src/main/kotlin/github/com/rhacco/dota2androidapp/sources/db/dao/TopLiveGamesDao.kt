package github.com.rhacco.dota2androidapp.sources.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import io.reactivex.Flowable

@Dao
interface TopLiveGamesDao {
    @Query("SELECT * FROM top_live_games")
    fun loadAllTopLiveGames(): Flowable<List<TopLiveGameEntity>>

    @Update
    fun updateTopLiveGames(products: MutableList<TopLiveGameEntity>)
}
