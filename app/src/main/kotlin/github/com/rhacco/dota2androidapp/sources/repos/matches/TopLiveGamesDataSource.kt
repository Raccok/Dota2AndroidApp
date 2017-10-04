package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import io.reactivex.Single

interface TopLiveGamesDataSource {
    fun getTopLiveGames(): Single<List<TopLiveGameEntity>>
}
