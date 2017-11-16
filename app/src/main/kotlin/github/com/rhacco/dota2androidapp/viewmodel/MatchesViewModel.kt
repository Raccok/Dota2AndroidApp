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
import github.com.rhacco.dota2androidapp.sources.repos.matches.RealtimeStatsRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.TopLiveGamesRepository
import github.com.rhacco.dota2androidapp.sources.repos.players.ProPlayersRepository
import io.reactivex.disposables.CompositeDisposable

class MatchesViewModel(application: Application) : AndroidViewModel(application) {
    private val mIsLoading = MediatorLiveData<Boolean>()
    private val mDisposables = CompositeDisposable()
    private val mProPlayersChecked: MutableList<Long> = mutableListOf()
    private val mHeroesSet: MutableList<Long> = mutableListOf()
    private val mFinishedMatches: MutableList<Long> = mutableListOf()
    val mLiveMatchesQuery = MediatorLiveData<List<TopLiveGamesResponse.Game>>()
    val mLiveMatchesItemDataQuery = MediatorLiveData<LiveMatchesItemData>()
    val mCheckProPlayersQuery = MediatorLiveData<Pair<Long, List<ProPlayerEntity>>>()
    val mHeroesQuery = MediatorLiveData<Pair<Long, List<HeroEntity>>>()
    val mCheckMatchFinishedQuery = MediatorLiveData<Long>()

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
                            if (mFinishedMatches.contains(result.match.matchid))
                                return@subscribe
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
                                            playerSteamIds.add(it.accountid)
                                            heroIds.add(it.heroid)
                                        }
                                    else
                                        Log.d(App.instance.getString(R.string.log_msg_debug),
                                                "GetRealtimeStats returned corrupted players data")
                                }
                                if (heroIds.size == 10 && heroIds[0] != 0 && heroIds[1] != 0)
                                    newItemData.mHeroesAssigned = true
                                if (newItemData.mHeroesAssigned)
                                    heroIds.forEach { newItemData.mHeroes.add(Hero(it)) }
                                newItemData.mServerId = matchBaseVals.server_steam_id
                                newItemData.mMatchId = result.match.matchid
                                if (newItemData.mMatchId > 0) {
                                    mLiveMatchesItemDataQuery.value = newItemData
                                    if (!mProPlayersChecked.contains(newItemData.mMatchId)) {
                                        checkProPlayers(newItemData.mMatchId, playerSteamIds)
                                        mProPlayersChecked.add(newItemData.mMatchId)
                                    }
                                    if (newItemData.mHeroesAssigned &&
                                            !mHeroesSet.contains(newItemData.mMatchId)) {
                                        getHeroes(newItemData.mMatchId, heroIds)
                                        mHeroesSet.add(newItemData.mMatchId)
                                    }
                                }
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

    private fun checkProPlayers(matchId: Long, playerSteamIds: List<Long>) =
            mDisposables.add(ProPlayersRepository.checkProPlayers(playerSteamIds)
                    .subscribe(
                            { result -> mCheckProPlayersQuery.value = Pair(matchId, result) },
                            { error ->
                                mProPlayersChecked.remove(matchId)
                                Log.d(App.instance.getString(R.string.log_msg_debug),
                                        "Failed to fetch pro players: " + error)
                            }
                    ))

    private fun getHeroes(matchId: Long, heroIds: List<Int>) =
            mDisposables.add(HeroesRepository.getHeroesByIds(heroIds)
                    .subscribe(
                            { result -> mHeroesQuery.value = Pair(matchId, result) },
                            { error ->
                                mHeroesSet.remove(matchId)
                                Log.d(App.instance.getString(R.string.log_msg_debug),
                                        "Failed to fetch heroes: " + error)
                            }
                    ))

    fun checkMatchFinished(matchId: Long) {
        mIsLoading.value = true
        mDisposables.add(MatchDetailsRepository.getMatchDetails(matchId)
                .subscribe(
                        { result ->
                            mIsLoading.value = false
                            // The official Dota 2 API returns nothing for 'error' when match
                            // details *are* present for given matchId, i.e. when the match is
                            // finished, so 'error' is null in this case
                            if (result.error == null) {
                                mProPlayersChecked.remove(matchId)
                                mHeroesSet.remove(matchId)
                                mFinishedMatches.add(matchId)
                                mCheckMatchFinishedQuery.value = matchId
                            }
                        },
                        { error ->
                            mIsLoading.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch match details: " + error)
                        }
                ))
    }
}
