package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.MatchDetailsResponse
import github.com.rhacco.dota2androidapp.sources.remote.Dota2OfficialAPIService
import io.reactivex.Single

object MatchDetailsRemoteDataSource : MatchDetailsDataSource {
    override fun getMatchDetails(matchId: Long): Single<MatchDetailsResponse.Values> =
            Single.create(
                    { subscriber ->
                        Dota2OfficialAPIService.get()
                                ?.fetchMatchDetails(App.instance.getString(R.string.api_key), matchId)
                                ?.map { it.component1() }
                                ?.subscribe(
                                        { result -> subscriber.onSuccess(result) },
                                        { error -> subscriber.onError(error) }
                                )
                    }
            )
}
