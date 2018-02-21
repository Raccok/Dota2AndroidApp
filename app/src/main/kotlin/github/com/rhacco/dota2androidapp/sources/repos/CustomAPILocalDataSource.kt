package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import github.com.rhacco.dota2androidapp.sources.databases.entities.HeroEntity
import io.reactivex.Single

object CustomAPILocalDataSource {
    fun getHeroes(): Single<List<HeroEntity>> =
            Single.create(
                    { subscriber ->
                        App.sDatabase.getHeroes()
                                .subscribe(
                                        { result ->
                                            if (result.isNotEmpty() &&
                                                    App.sSharedPreferences.getHeroesValid())
                                                subscriber.onSuccess(result)
                                            else
                                                CustomAPIRemoteDataSource.getHeroes()
                                                        .subscribe(
                                                                { remoteResult ->
                                                                    App.sDatabase.storeHeroes(remoteResult)
                                                                    App.sSharedPreferences.setHeroesValid()
                                                                    subscriber.onSuccess(remoteResult)
                                                                },
                                                                { error -> subscriber.onError(error) }
                                                        )
                                        },
                                        { error -> subscriber.onError(error) }
                                )
                    }
            )

    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> =
            Single.create(
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