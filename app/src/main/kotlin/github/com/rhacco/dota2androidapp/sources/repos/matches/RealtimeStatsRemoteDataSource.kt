package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import github.com.rhacco.dota2androidapp.sources.remote.sDota2OfficialAPIService
import io.reactivex.Single

object RealtimeStatsRemoteDataSource : RealtimeStatsDataSource {
    override fun getRealtimeStats(serverSteamId: Long): Single<List<RealtimeStatsEntity>> =
            Single.create(
                    { subscriber ->
                        sDota2OfficialAPIService
                                .fetchRealtimeStats(App.instance.getString(R.string.api_key), serverSteamId)
                                .subscribe(
                                        { result -> subscriber.onSuccess(listOf(result.match)) },
                                        { _ -> subscriber.onError(Exception()) })
                    }
            )
}
