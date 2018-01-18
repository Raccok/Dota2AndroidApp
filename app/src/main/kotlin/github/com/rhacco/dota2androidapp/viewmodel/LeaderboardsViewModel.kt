package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import github.com.rhacco.dota2androidapp.sources.repos.Dota2APIRepository
import io.reactivex.disposables.CompositeDisposable

class LeaderboardsViewModel(application: Application) : AndroidViewModel(application) {
    private val mDisposables = CompositeDisposable()
    val mGetLeaderboard = MediatorLiveData<List<String>>()

    override fun onCleared() = mDisposables.clear()

    fun getLeaderboard(region: String) {
        mDisposables.add(Dota2APIRepository.getLeaderboard(region)
                .subscribe(
                        { result -> mGetLeaderboard.value = convert(result.leaderboard) },
                        { error ->
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch a leaderboard: " + error)
                        }
                ))
    }

    private fun convert(leaderboard: List<LeaderboardsResponse.Entry>): List<String> {
        val convertedLeaderboard: MutableList<String> = mutableListOf()
        leaderboard.forEachIndexed { index, entry ->
            if (index == 500)
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