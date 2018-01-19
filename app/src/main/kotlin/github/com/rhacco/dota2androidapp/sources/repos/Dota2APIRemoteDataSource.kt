package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import github.com.rhacco.dota2androidapp.sources.remote.Dota2APIService
import io.reactivex.Single

object Dota2APIRemoteDataSource {
    fun getLeaderboard(region: String): Single<List<String>> =
            Single.create(
                    { subscriber ->
                        Dota2APIService.get()?.fetchLeaderboard(region)?.subscribe(
                                { result ->
                                    val converted = convert(result.leaderboard)
                                    App.sSharedPreferences.storeLeaderboard(region, converted)
                                    subscriber.onSuccess(convert(result.leaderboard))
                                },
                                { error -> subscriber.onError(error) }
                        )
                    }
            )

    private fun convert(leaderboard: List<LeaderboardsResponse.Entry>): List<String> {
        val convertedLeaderboard: MutableList<String> = mutableListOf()
        leaderboard.forEachIndexed { index, entry ->
            if (index == 2000)
                return convertedLeaderboard
            val convertedEntry =
                    if (entry.team_tag != null && entry.team_tag.isNotEmpty())
                        entry.team_tag + "." + entry.name
                    else
                        entry.name
            convertedLeaderboard.add(convertedEntry)
        }
        return convertedLeaderboard
    }
}