package github.com.rhacco.dota2androidapp.sources.repos.players

import github.com.rhacco.dota2androidapp.api.ProPlayersResponse
import io.reactivex.Single

object ProPlayersLocalDataSource {
    private val mProPlayers: MutableMap<Long, ProPlayersResponse.ProPlayer> = mutableMapOf()

    fun getOfficialName(steamAccountId: Long): Single<String> =
            Single.create(
                    { subscriber ->
                        when {
                            mProPlayers.isEmpty() ->
                                ProPlayersRemoteDataSource.getProPlayers()
                                        .subscribe(
                                                { result ->
                                                    saveProPlayers(result)
                                                    if (mProPlayers.containsKey(steamAccountId))
                                                        subscriber.onSuccess(mProPlayers[steamAccountId]!!.name)
                                                    else
                                                        subscriber.onSuccess("")
                                                },
                                                { error -> subscriber.onError(error) }
                                        )
                            mProPlayers.containsKey(steamAccountId) ->
                                subscriber.onSuccess(mProPlayers[steamAccountId]!!.name)
                            else ->
                                subscriber.onSuccess("")
                        }
                    }
            )

    private fun saveProPlayers(proPlayers: List<ProPlayersResponse.ProPlayer>) {
        for (player in proPlayers)
            mProPlayers[player.account_id] = player
    }
}
