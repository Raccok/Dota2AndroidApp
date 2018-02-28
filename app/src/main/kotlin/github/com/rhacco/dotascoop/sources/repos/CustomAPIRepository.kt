package github.com.rhacco.dotascoop.sources.repos

import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.api.LeaderboardsResponse
import github.com.rhacco.dotascoop.api.TopMatchesResponse
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object CustomAPIRepository {
    fun getTopLiveMatches(): Single<List<TopMatchesResponse.Match>> =
            CustomAPIRemoteDataSource.getTopLiveMatches()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getTopRecentMatches(): Single<List<TopMatchesResponse.Match>> =
            CustomAPIRemoteDataSource.getTopRecentMatches()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getHeroes(): Single<List<HeroEntity>> =
            CustomAPILocalDataSource.getHeroes()
                    .onErrorResumeNext {
                        CustomAPIRemoteDataSource.getHeroes()
                                .doOnSuccess { result ->
                                    App.sDatabase.storeHeroes(result)
                                    App.sSharedPreferences.setHeroesLastUpdate()
                                }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getItems(): Single<List<ItemEntity>> =
            CustomAPILocalDataSource.getItems()
                    .onErrorResumeNext {
                        CustomAPIRemoteDataSource.getItems()
                                .doOnSuccess { result ->
                                    App.sDatabase.storeItems(result)
                                    App.sSharedPreferences.setItemsLastUpdate()
                                }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> =
            CustomAPILocalDataSource.getLeaderboard(region)
                    .onErrorResumeNext {
                        CustomAPIRemoteDataSource.getLeaderboard(region)
                                .doOnSuccess { result ->
                                    App.sDatabase.storeLeaderboard(region, result)
                                    App.sSharedPreferences.setLeaderboardLastUpdate(region)
                                }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}