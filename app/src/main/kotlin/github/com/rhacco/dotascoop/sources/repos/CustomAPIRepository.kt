package github.com.rhacco.dotascoop.sources.repos

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
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getItems(): Single<List<ItemEntity>> =
            CustomAPILocalDataSource.getItems()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> =
            CustomAPILocalDataSource.getLeaderboard(region)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}