package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object Dota2APIRepository {
    fun getLeaderboard(region: String): Single<LeaderboardsResponse.Result> =
            Dota2APIRemoteDataSource.getLeaderboard(region)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}