package github.com.rhacco.dota2androidapp.sources.repos.matches

import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import github.com.rhacco.dota2androidapp.sources.remote.sDota2OfficialAPIService
import io.reactivex.Single

object RealtimeStatsRemoteDataSource : RealtimeStatsDataSource {
    override fun getRealtimeStats(serverSteamId: Long): Single<RealtimeStatsEntity> =
            Single.create(
                    { subscriber ->
                        Log.d("QUERY_INIT", "remote data source")
                        sDota2OfficialAPIService
                                .fetchRealtimeStats(App.instance.getString(R.string.api_key), serverSteamId)
                                .subscribe(
                                        { result ->
                                            Log.d("QUERY_SUCCESS", "remote data source")
                                            subscriber.onSuccess(result.match)
                                        },
                                        { _ ->
                                            Log.d("QUERY_ERROR", "remote data source")
                                            subscriber.onError(Exception())
                                        })
                    }
            )
}
