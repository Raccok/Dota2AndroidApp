package github.com.rhacco.dota2androidapp.sources.repos.players

import github.com.rhacco.dota2androidapp.api.ProPlayersResponse
import io.reactivex.Single

object ProPlayersLocalDataSource {
    private val mProPlayers: MutableMap<Long, ProPlayersResponse.ProPlayer> = mutableMapOf()

    fun checkProPlayers(playerSteamIds: List<Long>): Single<List<ProPlayersResponse.ProPlayer>> =
            Single.create(
                    { subscriber ->
                        if (mProPlayers.isEmpty())
                            ProPlayersRemoteDataSource.getProPlayers()
                                    .subscribe(
                                            { result ->
                                                saveProPlayers(result)
                                                subscriber.onSuccess(checkProPlayersHelper(playerSteamIds))
                                            },
                                            { error -> subscriber.onError(error) }
                                    )
                        else
                            subscriber.onSuccess(checkProPlayersHelper(playerSteamIds))
                    }
            )

    private fun saveProPlayers(proPlayers: List<ProPlayersResponse.ProPlayer>) {
        for (player in proPlayers)
            mProPlayers[player.account_id] = player
    }

    private fun checkProPlayersHelper(playerSteamIds: List<Long>): List<ProPlayersResponse.ProPlayer> {
        val result: MutableList<ProPlayersResponse.ProPlayer> = mutableListOf()
        playerSteamIds.forEach {
            if (mProPlayers.containsKey(it))
                result.add(mProPlayers[it]!!)
        }
        return result
    }
}
