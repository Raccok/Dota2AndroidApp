package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.lists.LiveMatchesListAdapter
import github.com.rhacco.dota2androidapp.sources.repos.matches.RealtimeStatsRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.TopLiveGamesRepository
import io.reactivex.disposables.CompositeDisposable

class MatchesViewModel(application: Application) : AndroidViewModel(application) {
    private val mIsLoadingLiveData = MediatorLiveData<Boolean>()
    private val mDisposables = CompositeDisposable()
    val mLiveMatchesQueryLiveData = MediatorLiveData<List<TopLiveGamesResponse.Game>>()
    val mLiveMatchListDataQueryLiveData = MediatorLiveData<LiveMatchesListAdapter.ListItemData>()

    override fun onCleared() = mDisposables.clear()

    fun getLiveMatches() {
        mIsLoadingLiveData.value = true
        mDisposables.add(TopLiveGamesRepository.getTopLiveGames()
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            mLiveMatchesQueryLiveData.value = result
                        },
                        { error ->
                            mIsLoadingLiveData.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch top live games: " + error)
                        }
                ))
    }

    fun getLiveMatchListData(averageMMR: Int, serverSteamId: Long) {
        mIsLoadingLiveData.value = true
        mDisposables.add(RealtimeStatsRepository.getRealtimeStats(serverSteamId)
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            val listItemData: LiveMatchesListAdapter.ListItemData =
                                    LiveMatchesListAdapter.ListItemData()
                            listItemData.mAverageMMR = averageMMR
                            if (averageMMR < 1)
                                listItemData.mTitle = "Tournament Match (Match ID " +
                                        result.match.matchid + ")"
                            else
                                listItemData.mTitle = "Ranked Match (Average MMR " + averageMMR +
                                        ", Match ID " + result.match.matchid + ")"
                            if (result.teams.size == 2) {
                                result.teams[0].players
                                        .asSequence()
                                        .map {
                                            if (it.name.length > 20)
                                                it.name.substring(0, 16) + "..."
                                            else
                                                it.name
                                        }
                                        .forEach {
                                            listItemData.mPlayersRadiant =
                                                    listItemData.mPlayersRadiant + "\n" + it
                                        }
                                result.teams[1].players
                                        .asSequence()
                                        .map {
                                            if (it.name.length > 20)
                                                it.name.substring(0, 16) + "..."
                                            else
                                                it.name
                                        }
                                        .forEach {
                                            listItemData.mPlayersDire =
                                                    listItemData.mPlayersDire + "\n" + it
                                        }
                                mLiveMatchListDataQueryLiveData.value = listItemData
                            } else
                                Log.d(App.instance.getString(R.string.log_msg_debug),
                                        "GetRealtimeStats returned " + result.teams.size + " teams")
                        },
                        { error ->
                            mIsLoadingLiveData.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch realtime stats: " + error)
                        }
                ))
    }
}
