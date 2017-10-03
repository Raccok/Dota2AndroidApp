package github.com.rhacco.dota2androidapp.sources.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import io.reactivex.Flowable

@Dao
interface RealtimeStatsDao {
    @Query("SELECT * FROM realtime_stats WHERE server_steam_id = :serverSteamId")
    fun loadRealtimeStats(serverSteamId: Long): Flowable<RealtimeStatsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RealtimeStatsEntity)
}
