package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.lists.LiveMatchesItemData
import github.com.rhacco.dota2androidapp.lists.Player
import github.com.rhacco.dota2androidapp.sources.repos.matches.MatchDetailsRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.RealtimeStatsLocalDataSource
import github.com.rhacco.dota2androidapp.sources.repos.matches.RealtimeStatsRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.TopLiveGamesRepository
import github.com.rhacco.dota2androidapp.sources.repos.players.ProPlayersRepository
import io.reactivex.disposables.CompositeDisposable

class MatchesViewModel(application: Application) : AndroidViewModel(application) {
    private val mIsLoading = MediatorLiveData<Boolean>()
    private val mDisposables = CompositeDisposable()
    val mLiveMatchesQuery = MediatorLiveData<List<TopLiveGamesResponse.Game>>()
    val mLiveMatchesItemDataQuery = MediatorLiveData<LiveMatchesItemData>()
    val mOfficialNameQuery = MediatorLiveData<Pair<Long, String>>()
    val mCheckMatchFinishedQuery = MediatorLiveData<Pair<Long, Boolean>>()
    val mRemoveFinishedMatchQuery = MediatorLiveData<Long>()

    override fun onCleared() = mDisposables.clear()

    fun getLiveMatches() {
        mIsLoading.value = true
        mDisposables.add(TopLiveGamesRepository.getTopLiveGames()
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            mLiveMatchesQuery.value = result
                        },
                        { error ->
                            mIsLoading.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch top live games: " + error)
                        }
                ))
    }

    fun getLiveMatchesItemData(averageMMR: Int, serverSteamId: Long) {
        mIsLoading.value = true
        mDisposables.add(RealtimeStatsRepository.getRealtimeStats(serverSteamId)
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            val newItemData = LiveMatchesItemData()
                            newItemData.mMatchID = result.match.matchid
                            newItemData.mAverageMMR = averageMMR
                            if (averageMMR < 1)
                                newItemData.mTitle = App.instance.getString(
                                        R.string.heading_live_tournament_match, result.match.matchid)
                            else
                                newItemData.mTitle = App.instance.getString(
                                        R.string.heading_live_ranked_match, averageMMR, result.match.matchid)
                            if (result.teams?.size == 2) {
                                for (team in result.teams) {
                                    val steamAccountIds = mutableListOf<Long>()
                                    if (team.players?.size == 5)
                                        team.players.forEach {
                                            newItemData.mPlayers.add(Player(it.accountid, it.name))
                                            steamAccountIds.add(it.accountid)
                                        }
                                    else
                                        Log.d(App.instance.getString(R.string.log_msg_debug),
                                                "GetRealtimeStats returned corrupted players data")
                                    getOfficialNames(steamAccountIds)
                                }
                                mLiveMatchesItemDataQuery.value = newItemData
                            } else
                                Log.d(App.instance.getString(R.string.log_msg_debug),
                                        "GetRealtimeStats returned corrupted teams data")
                        },
                        { error ->
                            mIsLoading.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch realtime stats: " + error)
                        }
                ))
    }

    private fun getOfficialNames(steamAccountIds: List<Long>) {
        if (steamAccountIds.isEmpty())
            return
        mIsLoading.value = true
        // The initial query takes a while (fetches quite a big list from OpenDota API and saves it
        // locally) so wait for that before continuing with other players
        mDisposables.add(ProPlayersRepository.getOfficialName(steamAccountIds[0])
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            if (result.isNotEmpty())
                                mOfficialNameQuery.value = Pair(steamAccountIds[0], result)
                            steamAccountIds.forEachIndexed { index, steamAccountId ->
                                if (index > 0)
                                    getOfficialName(steamAccountId)
                            }
                        },
                        { error ->
                            mIsLoading.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch official player name (1): " + error)
                        }
                ))
    }

    private fun getOfficialName(steamAccountId: Long) {
        mIsLoading.value = true
        mDisposables.add(ProPlayersRepository.getOfficialName(steamAccountId)
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            if (result.isNotEmpty())
                                mOfficialNameQuery.value = Pair(steamAccountId, result)
                        },
                        { error ->
                            mIsLoading.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch official player name (2): " + error)
                        }
                ))
    }

    fun checkMatchFinished(matchId: Long) {
        mIsLoading.value = true
        mDisposables.add(MatchDetailsRepository.getMatchDetails(matchId)
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            // The official Dota 2 API returns nothing for 'error' when match
                            // details *are* present for given matchId, i.e. when the match is
                            // finished, so 'error' is null in this case
                            mCheckMatchFinishedQuery.value = Pair(matchId, result.error == null)
                        },
                        { error ->
                            mIsLoading.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch match details: " + error)
                        }
                ))
    }

    fun removeFinishedMatch(matchId: Long) {
        RealtimeStatsLocalDataSource.removeRealtimeStats(matchId)
        mRemoveFinishedMatchQuery.value = matchId
    }
}
