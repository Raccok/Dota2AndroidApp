package github.com.rhacco.dota2androidapp.sources.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import io.reactivex.Flowable

@Dao
interface ProPlayersDao {
    @Query("SELECT * FROM proPlayers")
    fun loadAllProPlayers(): Flowable<List<ProPlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProPlayerEntity>)
}
