package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.lists.LiveMatchesItemData
import github.com.rhacco.dota2androidapp.sources.repos.matches.RealtimeStatsRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.TopLiveGamesRepository
import io.reactivex.disposables.CompositeDisposable

class MatchesViewModel(application: Application) : AndroidViewModel(application) {
    private val mIsLoading = MediatorLiveData<Boolean>()
    private val mDisposables = CompositeDisposable()
    val mLiveMatchesQuery = MediatorLiveData<List<TopLiveGamesResponse.Game>>()
    val mLiveMatchesItemDataQuery = MediatorLiveData<LiveMatchesItemData>()

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
                            newItemData.mAverageMMR = averageMMR
                            if (averageMMR < 1)
                                newItemData.mTitle = "Tournament Match (Match ID " +
                                        result.match.matchid + ")"
                            else
                                newItemData.mTitle = "Ranked Match (Average MMR " + averageMMR +
                                        ", Match ID " + result.match.matchid + ")"
                            if (result.teams.size == 2 &&
                                    result.teams[0].players.size == 5 &&
                                    result.teams[1].players.size == 5) {
                                val radiantPlayers = result.teams[0].players
                                val direPlayers = result.teams[1].players
                                newItemData.mBlue = radiantPlayers[0].name
                                newItemData.mTeal = radiantPlayers[1].name
                                newItemData.mPurple = radiantPlayers[2].name
                                newItemData.mYellow = radiantPlayers[3].name
                                newItemData.mOrange = radiantPlayers[4].name
                                newItemData.mPink = direPlayers[0].name
                                newItemData.mGray = direPlayers[1].name
                                newItemData.mLightBlue = direPlayers[2].name
                                newItemData.mDarkGreen = direPlayers[3].name
                                newItemData.mBrown = direPlayers[4].name
                                mLiveMatchesItemDataQuery.value = newItemData
                            } else
                                Log.d(App.instance.getString(R.string.log_msg_debug),
                                        "GetRealtimeStats returned a wrong number of teams/players")
                        },
                        { error ->
                            mIsLoading.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch realtime stats: " + error)
                        }
                ))
    }
}
