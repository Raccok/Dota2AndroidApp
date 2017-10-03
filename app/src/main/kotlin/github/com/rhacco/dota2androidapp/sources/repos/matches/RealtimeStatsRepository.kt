package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RealtimeStatsRepository : RealtimeStatsDataSource {
    override fun getRealtimeStats(serverSteamId: Long): Single<List<RealtimeStatsEntity>> =
            RealtimeStatsLocalDataSource.getRealtimeStats(serverSteamId)
                    .onErrorResumeNext {
                        RealtimeStatsRemoteDataSource.getRealtimeStats(serverSteamId)
                                .doOnSuccess { RealtimeStatsLocalDataSource.updateRealtimeStats(it) }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
