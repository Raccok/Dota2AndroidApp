package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import io.reactivex.Single

interface TopLiveGamesDataSource {
    fun getTopLiveGames(): Single<TopLiveGamesResponse.Result>
}
