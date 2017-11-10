package github.com.rhacco.dota2androidapp.sources.repos.players

import github.com.rhacco.dota2androidapp.api.ProPlayersResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ProPlayersRepository {
    fun checkProPlayers(playerSteamIds: List<Long>): Single<List<ProPlayersResponse.ProPlayer>> =
            ProPlayersLocalDataSource.checkProPlayers(playerSteamIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
