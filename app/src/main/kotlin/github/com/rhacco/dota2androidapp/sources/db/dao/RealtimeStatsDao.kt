package github.com.rhacco.dota2androidapp.sources.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import io.reactivex.Flowable

@Dao
interface RealtimeStatsDao {
    @Query("SELECT * FROM realtime_stats WHERE server_steam_id = :serverSteamId")
    fun loadRealtimeStats(serverSteamId: Long): Flowable<List<RealtimeStatsEntity>>

    @Update
    fun updateRealtimeStats(list: List<RealtimeStatsEntity>)
}
