package github.com.rhacco.dota2androidapp.sources.repos.players

import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import github.com.rhacco.dota2androidapp.sources.db.DatabaseCreator
import io.reactivex.Single

object ProPlayersLocalDataSource {
    private val mProPlayersDao = DatabaseCreator.mDatabase.proPlayersDao()

    fun checkProPlayers(playerSteamIds: List<Long>): Single<List<ProPlayerEntity>> =
            Single.create(
                    { subscriber ->
                        mProPlayersDao.loadAllProPlayers()
                                .subscribe(
                                        { result ->
                                            if (result.isEmpty())
                                                ProPlayersRemoteDataSource.getProPlayers()
                                                        .subscribe(
                                                                { remoteResult ->
                                                                    mProPlayersDao.insertAll(remoteResult)
                                                                    subscriber.onSuccess(
                                                                            checkProPlayersHelper(
                                                                                    playerSteamIds,
                                                                                    remoteResult))
                                                                },
                                                                { error -> subscriber.onError(error) }
                                                        )
                                            else
                                                subscriber.onSuccess(
                                                        checkProPlayersHelper(playerSteamIds, result))
                                        },
                                        { error -> subscriber.onError(error) }
                                )
                    }
            )

    private fun checkProPlayersHelper(playerSteamIds: List<Long>, proPlayers: List<ProPlayerEntity>):
            List<ProPlayerEntity> {
        val result: MutableList<ProPlayerEntity> = mutableListOf()
        playerSteamIds.forEach { playerSteamId ->
            proPlayers.forEach { proPlayer ->
                if (playerSteamId == proPlayer.account_id)
                    result.add(proPlayer)
            }
        }
        return result
    }
}
