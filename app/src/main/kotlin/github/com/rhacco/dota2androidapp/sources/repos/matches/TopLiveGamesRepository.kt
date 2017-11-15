package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object TopLiveGamesRepository {
    fun getTopLiveGames(): Single<List<TopLiveGamesResponse.Game>> =
            TopLiveGamesRemoteDataSource.getTopLiveGames()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
