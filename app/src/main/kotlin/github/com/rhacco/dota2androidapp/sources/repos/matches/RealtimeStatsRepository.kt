package github.com.rhacco.dota2androidapp.sources.repos.matches

import android.util.Log
import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RealtimeStatsRepository : RealtimeStatsDataSource {
    override fun getRealtimeStats(serverSteamId: Long): Single<List<RealtimeStatsEntity>> =
            RealtimeStatsRemoteDataSource.getRealtimeStats(serverSteamId)
                    .doOnSuccess {
                        Log.d("QUERY_INIT", "completed error")
                        RealtimeStatsLocalDataSource.updateRealtimeStats(it)
                    }
                                .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
