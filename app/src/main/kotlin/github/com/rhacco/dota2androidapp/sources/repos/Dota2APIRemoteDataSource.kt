package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import github.com.rhacco.dota2androidapp.sources.remote.Dota2APIService
import io.reactivex.Single

object Dota2APIRemoteDataSource {
    fun getLeaderboard(region: String): Single<LeaderboardsResponse.Result> =
            Single.create(
                    { subscriber ->
                        Dota2APIService.get()?.fetchLeaderboard(region)?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )
}