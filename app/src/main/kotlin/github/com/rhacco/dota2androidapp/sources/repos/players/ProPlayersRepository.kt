package github.com.rhacco.dota2androidapp.sources.repos.players

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ProPlayersRepository {
    fun getOfficialName(steamAccountId: Long): Single<String> =
            ProPlayersLocalDataSource.getOfficialName(steamAccountId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
