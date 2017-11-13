package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import github.com.rhacco.dota2androidapp.lists.Hero
import github.com.rhacco.dota2androidapp.lists.LiveMatchesItemData
import github.com.rhacco.dota2androidapp.lists.Player
import github.com.rhacco.dota2androidapp.sources.repos.heroes.HeroesRepository
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
    val mHeroesQuery = MediatorLiveData<Triple<Long, List<HeroEntity>, Int>>()
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

    fun getLiveMatchesItemData(matchBaseVals: TopLiveGamesResponse.Game) {
        mIsLoading.value = true
        mDisposables.add(RealtimeStatsRepository.getRealtimeStats(matchBaseVals.server_steam_id)
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            val newItemData = LiveMatchesItemData()
                            newItemData.mMatchBaseVals = matchBaseVals
                            if (matchBaseVals.average_mmr < 1)
                                newItemData.mIsTournamentMatch = true
                            else
                                newItemData.mTitle = App.instance.getString(
                                        R.string.heading_live_ranked_match, matchBaseVals.average_mmr)
                            if (result.graph_data?.graph_gold != null)
                                newItemData.mGoldAdvantage = result.graph_data.graph_gold.last()
                            else
                                Log.d(App.instance.getString(R.string.log_msg_debug),
                                        "GetRealtimeStats returned corrupted gold graph data")
                            newItemData.mElapsedTime = result.match.game_time
                            if (result.teams?.size == 2) {
                                newItemData.mRadiantScore = result.teams[0].score
                                newItemData.mDireScore = result.teams[1].score
                                val playerSteamIds = mutableListOf<Long>()
                                val heroIds = mutableListOf<Int>()
                                for (team in result.teams) {
                                    if (team.players?.size == 5)
                                        team.players.forEach {
                                            newItemData.mPlayers.add(Player(it.accountid, it.name))
                                            newItemData.mHeroes.add(Hero(it.heroid))
                                            playerSteamIds.add(it.accountid)
                                            heroIds.add(it.heroid)
                                        }
                                    else
                                        Log.d(App.instance.getString(R.string.log_msg_debug),
                                                "GetRealtimeStats returned corrupted players data")
                                }
                                newItemData.mServerId = matchBaseVals.server_steam_id
                                newItemData.mMatchId = result.match.matchid
                                checkProPlayers(playerSteamIds)
                                getHeroes(result.match.matchid, heroIds, result.match.game_time)
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

    private fun getHeroes(matchId: Long, heroIds: List<Int>, elapsedTime: Int) =
            HeroesRepository.getHeroesByIds(heroIds)
                    .subscribe({ result -> mHeroesQuery.value = Triple(matchId, result, elapsedTime) })

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
