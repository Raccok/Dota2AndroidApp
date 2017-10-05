package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.widget.Toast
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.sources.repos.heroes.HeroesRepository
import github.com.rhacco.dota2androidapp.utilities.deviceIsOnline
import io.reactivex.disposables.CompositeDisposable

open class HeroesViewModel(application: Application?) : AndroidViewModel(application) {
    // TODO: maybe organize the states of this into a base mViewModel?
    // This stateful observation seems pretty useful for any given API call we're going to make.
    private val mIsLoadingLiveData = MediatorLiveData<Boolean>()
    private val mThrowableLiveData = MediatorLiveData<Throwable>()
    private val mDisposables = CompositeDisposable()
    val mHeroesQueryLiveData = MediatorLiveData<Pair<String, List<HeroEntity>>>()

    override fun onCleared() = mDisposables.clear()

    fun getHero(hero: String) {
        mIsLoadingLiveData.value = true
        mDisposables.add(HeroesRepository.getHeroByLocalizedName(hero)
                .subscribe(
                        { result ->
                            mIsLoadingLiveData.value = false
                            mHeroesQueryLiveData.value = Pair(hero, result)
                        },
                        { error ->
                            if (!deviceIsOnline()) {
                                Toast.makeText(App.instance.applicationContext,
                                        App.instance.getString(R.string.error_no_internet),
                                        Toast.LENGTH_LONG).show()
                            }
                            mIsLoadingLiveData.value = false
                            mThrowableLiveData.value = error
                        })
        )
    }
}
