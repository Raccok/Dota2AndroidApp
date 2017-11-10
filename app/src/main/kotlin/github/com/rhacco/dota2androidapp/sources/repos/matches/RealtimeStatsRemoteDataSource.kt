package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.RealtimeStatsResponse
import github.com.rhacco.dota2androidapp.sources.remote.Dota2OfficialAPIService
import io.reactivex.Single

object RealtimeStatsRemoteDataSource : RealtimeStatsDataSource {
    override fun getRealtimeStats(serverSteamId: Long): Single<RealtimeStatsResponse.Result> =
            Single.create(
                    { subscriber ->
                        Dota2OfficialAPIService.get()
                                ?.fetchRealtimeStats(App.instance.getString(R.string.api_key), serverSteamId)
                                ?.subscribe(
                                        { result -> subscriber.onSuccess(result) },
                                        { error -> subscriber.onError(error) }
                                )
                    }
            )
}
