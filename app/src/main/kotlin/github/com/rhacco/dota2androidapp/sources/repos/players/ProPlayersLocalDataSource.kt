package github.com.rhacco.dota2androidapp.sources.repos.players

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import github.com.rhacco.dota2androidapp.sources.db.DatabaseCreator
import github.com.rhacco.dota2androidapp.utilities.SharedPreferencesHelper
import io.reactivex.Single
import java.util.*

object ProPlayersLocalDataSource {
    private val mProPlayersDao = DatabaseCreator.mDatabase.proPlayersDao()

    // First check for pro players in the app's database. If it doesn't have any or if the data is
    // older than one day, fetch pro players from the OpenDota API and update the database.
    fun checkProPlayers(playerSteamIds: List<Long>): Single<List<ProPlayerEntity>> =
            Single.create(
                    { subscriber ->
                        mProPlayersDao.loadAllProPlayers()
                                .subscribe(
                                        { result ->
                                            if (result.isEmpty() || !SharedPreferencesHelper(
                                                    App.instance.applicationContext).checkProPlayersValid())
                                                ProPlayersRemoteDataSource.getProPlayers()
                                                        .subscribe(
                                                                { remoteResult ->
                                                                    mProPlayersDao.insertAll(remoteResult)
                                                                    val cal = Calendar.getInstance()
                                                                    cal.add(Calendar.DAY_OF_MONTH, 1)
                                                                    val validDate = cal.time.toString()
                                                                    SharedPreferencesHelper(
                                                                            App.instance.applicationContext)
                                                                            .setProPlayersValidDate(validDate)
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
