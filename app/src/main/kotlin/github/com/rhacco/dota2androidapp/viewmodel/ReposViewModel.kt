package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.sources.repos.HeroesRepository
import io.reactivex.disposables.CompositeDisposable

open class ReposViewModel(application: Application?) : AndroidViewModel(application) {
    // TODO: maybe organize the states of this into a base mViewModel?
    // This stateful observation seems pretty useful for any given API call we're going to make.
    private val mIsLoadingLiveData = MediatorLiveData<Boolean>()
    private val mThrowableLiveData = MediatorLiveData<Throwable>()
    private val mDisposables = CompositeDisposable()
    val mHeroListSearchQueryLiveData = MediatorLiveData<Pair<String, List<HeroEntity>>>()

    override fun onCleared() = mDisposables.clear()

    fun getHero(hero: String) {
        mIsLoadingLiveData.value = true
        mDisposables.add(HeroesRepository
                .getHeroByLocalName(hero)
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            mHeroListSearchQueryLiveData.value = Pair(hero, result)
                        },
                        { error ->
                            mIsLoadingLiveData.value = false
                            mThrowableLiveData.value = error
                        })
        )
    }
}
