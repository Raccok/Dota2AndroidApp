package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.MatchDetailsResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object MatchDetailsRepository {
    fun getMatchDetails(matchId: Long): Single<MatchDetailsResponse.Values> =
            MatchDetailsRemoteDataSource.getMatchDetails(matchId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
