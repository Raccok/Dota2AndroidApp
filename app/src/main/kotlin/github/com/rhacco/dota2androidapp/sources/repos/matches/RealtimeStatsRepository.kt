package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.RealtimeStatsResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RealtimeStatsRepository : RealtimeStatsDataSource {
    override fun getRealtimeStats(serverSteamId: Long): Single<RealtimeStatsResponse.Result> =
            RealtimeStatsLocalDataSource.getRealtimeStats(serverSteamId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
