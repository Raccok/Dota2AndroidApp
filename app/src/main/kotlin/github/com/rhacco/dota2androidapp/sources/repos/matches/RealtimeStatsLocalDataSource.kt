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

    fun removeRealtimeStats(matchId: Long) {
        val values = mRealtimeStats.values.toList()
        var index = 0
        while (index < values.size) {
            val realtimeStats = values[index]
            if (realtimeStats.match.matchid == matchId) {
                mRealtimeStats.remove(realtimeStats.match.server_steam_id)
                TopLiveGamesLocalDataSource.removeTopLiveGame(realtimeStats.match.server_steam_id)
                return
            }
            ++index
        }
    }
}
