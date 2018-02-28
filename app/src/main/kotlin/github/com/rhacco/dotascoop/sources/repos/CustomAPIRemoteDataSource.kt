package github.com.rhacco.dotascoop.sources.repos

import github.com.rhacco.dotascoop.api.LastUpdatesResponse
import github.com.rhacco.dotascoop.api.LeaderboardsResponse
import github.com.rhacco.dotascoop.api.TopMatchesResponse
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import github.com.rhacco.dotascoop.sources.remote.CustomAPIService
import io.reactivex.Single

object CustomAPIRemoteDataSource {
    fun getTopLiveMatches(): Single<List<TopMatchesResponse.Match>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchTopLiveMatches()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )

    fun getTopRecentMatches(): Single<List<TopMatchesResponse.Match>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchTopRecentMatches()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )

    fun getHeroes(): Single<List<HeroEntity>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchHeroes()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )

    fun getItems(): Single<List<ItemEntity>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchItems()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )

    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchLeaderboard(region)?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )

    fun getLastUpdates(): Single<LastUpdatesResponse> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchLastUpdates()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )
}