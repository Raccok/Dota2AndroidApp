package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
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
    private var mIsLoadingProPlayers = false
    private val mDisposables = CompositeDisposable()
    private val mPlayersOnHold: MutableList<Long> = mutableListOf()
    val mLiveMatchesQuery = MediatorLiveData<List<TopLiveGamesResponse.Game>>()
    val mLiveMatchesItemDataQuery = MediatorLiveData<LiveMatchesItemData>()
    val mCheckProPlayersQuery = MediatorLiveData<List<ProPlayerEntity>>()
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

    fun getLiveMatchesItemData(match: TopLiveGamesResponse.Game) {
        mIsLoading.value = true
        mDisposables.add(RealtimeStatsRepository.getRealtimeStats(match.server_steam_id)
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            val newItemData = LiveMatchesItemData()
                            newItemData.mMatchID = result.match.matchid
                            newItemData.mAverageMMR = match.average_mmr
                            if (match.average_mmr < 1) {
                                newItemData.mTitle = App.instance.getString(
                                        R.string.heading_live_tournament_match, result.match.matchid)
                                newItemData.mTeamRadiant = match.team_name_radiant
                                newItemData.mTeamDire = match.team_name_dire
                            } else
                                newItemData.mTitle = App.instance.getString(
                                        R.string.heading_live_ranked_match,
                                        match.average_mmr, result.match.matchid)
                            if (result.teams?.size == 2) {
                                val playerSteamIds = mutableListOf<Long>()
                                for (team in result.teams) {
                                    if (team.players?.size == 5)
                                        team.players.forEach {
                                            newItemData.mPlayers.add(Player(it.accountid, it.name))
                                            playerSteamIds.add(it.accountid)
                                        }
                                    else
                                        Log.d(App.instance.getString(R.string.log_msg_debug),
                                                "GetRealtimeStats returned corrupted players data")
                                }
                                checkProPlayers(playerSteamIds)
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

    private fun checkProPlayers(playerSteamIds: List<Long>) {
        if (playerSteamIds.isEmpty())
            return
        if (mIsLoadingProPlayers)
            mPlayersOnHold.addAll(playerSteamIds)
        else {
            mIsLoadingProPlayers = true
            mDisposables.add(ProPlayersRepository.checkProPlayers(playerSteamIds)
                    .subscribe(
                            { result ->
                                mIsLoadingProPlayers = false
                                if (result.isNotEmpty())
                                    mCheckProPlayersQuery.value = result
                                // Pass a copy of mPlayersOnHold so we can clear them afterwards
                                checkProPlayers(mPlayersOnHold.toList())
                                mPlayersOnHold.clear()
                            },
                            { error ->
                                mIsLoadingProPlayers = false
                                Log.d(App.instance.getString(R.string.log_msg_debug),
                                        "Failed to fetch official player names: " + error)
                            }
                    ))
        }
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
