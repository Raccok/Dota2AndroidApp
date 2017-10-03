package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import github.com.rhacco.dota2androidapp.sources.db.DatabaseCreator
import io.reactivex.Single

object TopLiveGamesLocalDataSource : TopLiveGamesDataSource {
    private val mTopLiveGamesDao = DatabaseCreator.mDatabase.topLiveGamesDao()

    override fun getTopLiveGames(): Single<List<TopLiveGameEntity>> =
            mTopLiveGamesDao.loadAllTopLiveGames()
                    .firstOrError()
                    .doOnSuccess { if (it.isEmpty()) throw Exception() }

    override fun updateTopLiveGames(list: List<TopLiveGameEntity>) =
            mTopLiveGamesDao.updateTopLiveGames(list.toMutableList())
}
