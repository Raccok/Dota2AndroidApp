package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import io.reactivex.Single

interface RealtimeStatsDataSource {
    fun getRealtimeStats(serverSteamId: Long): Single<List<RealtimeStatsEntity>>
    fun updateRealtimeStats(list: List<RealtimeStatsEntity>) = Unit
}
