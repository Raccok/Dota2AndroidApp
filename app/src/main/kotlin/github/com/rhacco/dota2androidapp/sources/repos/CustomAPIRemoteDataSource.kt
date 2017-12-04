package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.api.TopMatchesResponse
import github.com.rhacco.dota2androidapp.sources.remote.CustomAPIService
import io.reactivex.Single

object CustomAPIRemoteDataSource {
    fun getTopLiveMatches(): Single<List<TopMatchesResponse.Match>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchTopLiveMatches()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )

    fun getTopRecentMatches(): Single<List<TopMatchesResponse.Match>> =
            Single.create(
                    { subscriber ->
                        CustomAPIService.get()?.fetchTopRecentMatches()?.subscribe(
                                { result -> subscriber.onSuccess(result) },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )
}