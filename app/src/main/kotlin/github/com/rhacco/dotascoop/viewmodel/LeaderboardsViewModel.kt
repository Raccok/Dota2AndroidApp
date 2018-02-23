package github.com.rhacco.dotascoop.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.api.LeaderboardsResponse
import github.com.rhacco.dotascoop.sources.repos.CustomAPIRepository
import io.reactivex.disposables.CompositeDisposable

class LeaderboardsViewModel(application: Application) : AndroidViewModel(application) {
    private val mDisposables = CompositeDisposable()
    val mGetLeaderboard = MediatorLiveData<List<LeaderboardsResponse.Entry>>()

    override fun onCleared() = mDisposables.clear()

    fun getLeaderboard(region: String) {
        mDisposables.add(CustomAPIRepository.getLeaderboard(region)
                .subscribe(
                        { result -> mGetLeaderboard.value = result },
                        { error ->
                            Log.d(App.instance.getString(R.string.log_target_debug),
                                    "Failed to fetch a leaderboard: " + error)
                        }
                ))
    }
}