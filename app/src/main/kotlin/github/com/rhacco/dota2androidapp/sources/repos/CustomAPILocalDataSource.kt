package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import io.reactivex.Single

object CustomAPILocalDataSource {
    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> {
        return Single.create(
                { subscriber ->
                    App.sDatabase.getLeaderboard(region)
                            .subscribe(
                                    { result ->
                                        if (result.isNotEmpty() &&
                                                App.sSharedPreferences.getLeaderboardValid(region))
                                            subscriber.onSuccess(result)
                                        else
                                            CustomAPIRemoteDataSource.getLeaderboard(region)
                                                    .subscribe(
                                                            { remoteResult ->
                                                                App.sDatabase.storeLeaderboard(
                                                                        region, remoteResult)
                                                                App.sSharedPreferences
                                                                        .setLeaderboardValid(region)
                                                                subscriber.onSuccess(remoteResult)
                                                            },
                                                            { error -> subscriber.onError(error) }
                                                    )
                                    },
                                    { error -> subscriber.onError(error) }
                            )
                }
        )
    }
}