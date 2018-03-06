package github.com.rhacco.dotascoop.sources.repos

import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.api.LeaderboardsResponse
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import io.reactivex.Single

object CustomAPILocalDataSource {
    fun getHeroes(): Single<List<HeroEntity>> = Single.create(
            { subscriber ->
                App.sDatabase.getHeroes().subscribe(
                        { result ->
                            CustomAPIRemoteDataSource.getLastUpdates().subscribe(
                                    { lastUpdates ->
                                        if (!App.sSharedPreferences.getHeroesNeedUpdate(
                                                lastUpdates.heroes) && result.isNotEmpty())
                                            subscriber.onSuccess(result)
                                        else
                                            subscriber.onError(Exception())
                                    },
                                    { error ->
                                        if (result.isNotEmpty())
                                            subscriber.onSuccess(result)
                                        else
                                            subscriber.onError(error)
                                    }
                            )
                        },
                        { error -> subscriber.onError(error) }
                )
            }
    )

    fun getItems(): Single<List<ItemEntity>> = Single.create(
            { subscriber ->
                App.sDatabase.getItems().subscribe(
                        { result ->
                            CustomAPIRemoteDataSource.getLastUpdates().subscribe(
                                    { lastUpdates ->
                                        if (!App.sSharedPreferences.getItemsNeedUpdate(
                                                lastUpdates.items) && result.isNotEmpty())
                                            subscriber.onSuccess(result)
                                        else
                                            subscriber.onError(Exception())
                                    },
                                    { error ->
                                        if (result.isNotEmpty())
                                            subscriber.onSuccess(result)
                                        else
                                            subscriber.onError(error)
                                    }
                            )
                        },
                        { error -> subscriber.onError(error) }
                )
            }
    )

    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> = Single.create(
            { subscriber ->
                App.sDatabase.getLeaderboard(region).subscribe(
                        { result ->
                            CustomAPIRemoteDataSource.getLastUpdates().subscribe(
                                    { lastUpdates ->
                                        if (!App.sSharedPreferences.getLeaderboardNeedsUpdate(
                                                region, lastUpdates.leaderboards) &&
                                                result.isNotEmpty())
                                            subscriber.onSuccess(result)
                                        else
                                            subscriber.onError(Exception())
                                    },
                                    { error ->
                                        if (result.isNotEmpty())
                                            subscriber.onSuccess(result)
                                        else
                                            subscriber.onError(error)
                                    }
                            )
                        },
                        { error -> subscriber.onError(error) }
                )
            }
    )
}