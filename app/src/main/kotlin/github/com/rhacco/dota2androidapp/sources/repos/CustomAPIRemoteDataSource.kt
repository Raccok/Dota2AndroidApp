package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.api.TopLiveMatchesResponse
import github.com.rhacco.dota2androidapp.sources.remote.CustomAPIService
import io.reactivex.Single

object CustomAPIRemoteDataSource {
    fun getTopLiveMatches(): Single<List<TopLiveMatchesResponse.Match>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchTopLiveMatches()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )
}