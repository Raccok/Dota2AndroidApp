package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import github.com.rhacco.dota2androidapp.sources.repos.heroes.HeroesRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.RealtimeStatsRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.TopLiveGamesRepository
import io.reactivex.disposables.CompositeDisposable

open class ReposViewModel(application: Application?) : AndroidViewModel(application) {
    // TODO: maybe organize the states of this into a base mViewModel?
    // This stateful observation seems pretty useful for any given API call we're going to make.
    private val mIsLoadingLiveData = MediatorLiveData<Boolean>()
    private val mThrowableLiveData = MediatorLiveData<Throwable>()
    private val mDisposables = CompositeDisposable()
    val mHeroesQueryLiveData = MediatorLiveData<Pair<String, List<HeroEntity>>>()
    val mTopLiveGamesQueryLiveData = MediatorLiveData<List<TopLiveGameEntity>>()
    val mRealtimeStatsQueryLiveData = MediatorLiveData<List<RealtimeStatsEntity>>()

    override fun onCleared() = mDisposables.clear()

    fun getHero(hero: String) {
        mIsLoadingLiveData.value = true
        mDisposables.add(HeroesRepository
                .getHeroByLocalizedName(hero)
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            mHeroesQueryLiveData.value = Pair(hero, result)
                        },
                        { error ->
                            mIsLoadingLiveData.value = false
                            mThrowableLiveData.value = error
                        })
        )
    }

    fun updateTopLiveGames() {
        mIsLoadingLiveData.value = true
        mDisposables.add(TopLiveGamesRepository
                .getTopLiveGames()
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            mTopLiveGamesQueryLiveData.value = result
                        },
                        { error ->
                            mIsLoadingLiveData.value = false
                            mThrowableLiveData.value = error
                        }
                ))
    }

    fun updateRealtimeStats(serverSteamId: Long) {
        mIsLoadingLiveData.value = true
        mDisposables.add(RealtimeStatsRepository
                .getRealtimeStats(serverSteamId)
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            mRealtimeStatsQueryLiveData.value = result
                        },
                        { error ->
                            mIsLoadingLiveData.value = false
                            mThrowableLiveData.value = error
                        }
                ))
    }
}
