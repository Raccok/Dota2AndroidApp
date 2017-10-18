package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.api.MatchDetailsResponse
import io.reactivex.Single

interface MatchDetailsDataSource {
    fun getMatchDetails(matchId: Long): Single<MatchDetailsResponse.Values>
}
