package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.sources.repos.matches.TopLiveGamesRepository
import io.reactivex.disposables.CompositeDisposable

open class TopLiveGamesViewModel(application: Application) : AndroidViewModel(application) {
    private val mIsLoadingLiveData = MediatorLiveData<Boolean>()
    private val mDisposables = CompositeDisposable()
    val mTopLiveGamesQueryLiveData = MediatorLiveData<List<TopLiveGamesResponse.Game>>()

    override fun onCleared() = mDisposables.clear()

    fun getTopLiveGames() {
        mIsLoadingLiveData.value = true
        mDisposables.add(TopLiveGamesRepository.getTopLiveGames()
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            // TODO string manipulation here i guess
                            mTopLiveGamesQueryLiveData.value = result
                        },
                        { error ->
                            mIsLoadingLiveData.value = false
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to update top live games: " + error)
                        }
                ))
    }
}
