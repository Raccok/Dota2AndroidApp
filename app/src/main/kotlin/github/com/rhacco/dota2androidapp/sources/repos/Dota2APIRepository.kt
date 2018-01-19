package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.App
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object Dota2APIRepository {
    fun getLeaderboard(region: String): Single<List<String>> {
        val cachedLeaderboard = App.sSharedPreferences.getLeaderboard(region)
        if (cachedLeaderboard.isNotEmpty())
            return Single.create({ subscriber -> subscriber.onSuccess(cachedLeaderboard) })
        return Dota2APIRemoteDataSource.getLeaderboard(region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}