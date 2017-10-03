package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object TopLiveGamesRepository : TopLiveGamesDataSource {
    override fun getTopLiveGames(): Single<List<TopLiveGameEntity>> =
            TopLiveGamesLocalDataSource.getTopLiveGames()
                    .onErrorResumeNext {
                        TopLiveGamesRemoteDataSource.getTopLiveGames()
                                .doOnSuccess { TopLiveGamesLocalDataSource.updateTopLiveGames(it) }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
