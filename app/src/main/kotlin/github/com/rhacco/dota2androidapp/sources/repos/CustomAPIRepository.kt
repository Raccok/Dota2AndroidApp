package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import github.com.rhacco.dota2androidapp.api.TopMatchesResponse
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

    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> =
            CustomAPILocalDataSource.getLeaderboard(region)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}