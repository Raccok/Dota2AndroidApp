package github.com.rhacco.dota2androidapp.sources.repos.players

import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import github.com.rhacco.dota2androidapp.sources.remote.OpenDotaAPIService
import io.reactivex.Single

object ProPlayersRemoteDataSource {
    fun getProPlayers(): Single<List<ProPlayerEntity>> =
            Single.create(
                    { subscriber ->
                        OpenDotaAPIService.get()
                                ?.fetchProPlayers()
                                ?.subscribe(
                                        { result -> subscriber.onSuccess(result) },
                                        { error -> subscriber.onError(error) }
                                )
                    }
            )
}
