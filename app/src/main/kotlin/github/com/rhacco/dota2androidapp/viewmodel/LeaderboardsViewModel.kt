package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.sources.repos.Dota2APIRepository
import io.reactivex.disposables.CompositeDisposable

class LeaderboardsViewModel(application: Application) : AndroidViewModel(application) {
    private val mDisposables = CompositeDisposable()
    val mGetLeaderboard = MediatorLiveData<List<String>>()

    override fun onCleared() = mDisposables.clear()

    fun getLeaderboard(region: String) {
        mDisposables.add(Dota2APIRepository.getLeaderboard(region)
                .subscribe(
                        { result -> mGetLeaderboard.value = result },
                        { error ->
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch a leaderboard: " + error)
                        }
                ))
    }
}