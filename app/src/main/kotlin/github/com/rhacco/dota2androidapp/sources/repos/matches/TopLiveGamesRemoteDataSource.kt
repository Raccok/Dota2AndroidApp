package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.sources.remote.Dota2OfficialAPIService
import io.reactivex.Single

object TopLiveGamesRemoteDataSource : TopLiveGamesDataSource {
    override fun getTopLiveGames(): Single<List<TopLiveGamesResponse.Game>> =
            Single.create(
                    { subscriber ->
                        Dota2OfficialAPIService.get()
                                ?.fetchTopLiveGames(App.instance.getString(R.string.api_key), 0)
                                ?.subscribe(
                                        { result -> subscriber.onSuccess(result.game_list) },
                                        { error -> subscriber.onError(error) }
                                )
                    }
            )
}
