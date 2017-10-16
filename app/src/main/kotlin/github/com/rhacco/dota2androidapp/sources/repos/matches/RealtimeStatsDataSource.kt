package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.RealtimeStatsResponse
import io.reactivex.Single

interface RealtimeStatsDataSource {
    fun getRealtimeStats(serverSteamId: Long): Single<RealtimeStatsResponse.Result>
    fun saveRealtimeStats(serverSteamId: Long, result: RealtimeStatsResponse.Result) = Unit
}
