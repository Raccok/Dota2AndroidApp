package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import io.reactivex.Single

object TopLiveGamesLocalDataSource : TopLiveGamesDataSource {
    private var mTopLiveGames: MutableList<TopLiveGamesResponse.Game> = mutableListOf()

    override fun getTopLiveGames(): Single<List<TopLiveGamesResponse.Game>> =
            Single.create(
                    { subscriber ->
                        if (mTopLiveGames.isNotEmpty())
                            subscriber.onSuccess(mTopLiveGames)
                        else
                            subscriber.onError(Exception())
                    }
            )

    override fun saveTopLiveGames(list: List<TopLiveGamesResponse.Game>) {
        mTopLiveGames = list.toMutableList()
    }

    override fun clearTopLiveGames() = mTopLiveGames.clear()
}
