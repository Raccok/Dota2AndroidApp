package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import io.reactivex.Single

object TopLiveGamesLocalDataSource : TopLiveGamesDataSource {
    private var mTopLiveGames: MutableMap<Long, TopLiveGamesResponse.Game> = mutableMapOf()

    override fun getTopLiveGames(): Single<List<TopLiveGamesResponse.Game>> =
            Single.create(
                    { subscriber ->
                        TopLiveGamesRemoteDataSource.getTopLiveGames()
                                .subscribe(
                                        { result ->
                                            saveTopLiveGames(result)
                                            subscriber.onSuccess(mTopLiveGames.values.toList())
                                        },
                                        { _ -> subscriber.onError(Exception()) }
                                )
                    })

    override fun saveTopLiveGames(list: List<TopLiveGamesResponse.Game>) {
        for (game in list)
            mTopLiveGames[game.server_steam_id] = game
    }
}
