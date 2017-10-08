package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.RealtimeStatsResponse
import io.reactivex.Single

object RealtimeStatsLocalDataSource : RealtimeStatsDataSource {
    private val mRealtimeStats: MutableMap<Long, RealtimeStatsResponse.Result> = mutableMapOf()

    override fun getRealtimeStats(serverSteamId: Long): Single<RealtimeStatsResponse.Result> =
            Single.create(
                    { subscriber ->
                        if (mRealtimeStats.containsKey(serverSteamId))
                            subscriber.onSuccess(mRealtimeStats[serverSteamId])
                        else
                            subscriber.onError(Exception())
                    }
            )

    override fun saveRealtimeStats(serverSteamId: Long, result: RealtimeStatsResponse.Result) {
        mRealtimeStats[serverSteamId] = result
    }

    override fun clearRealtimeStats() = mRealtimeStats.clear()
}
