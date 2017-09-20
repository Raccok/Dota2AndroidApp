package com.ik.exploringviewmodel.flow.repos

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import github.com.raccok.dota2androidapp.entities.HeroEntity
import github.com.raccok.dota2androidapp.sources.repos.HerosRepository
import io.reactivex.disposables.CompositeDisposable

open class ReposViewModel(application: Application?) : AndroidViewModel(application) {

    //TODO: maybe organize the states this into a base viewModel? This stateful observation seems pretty useful for any given
    //API call we're going to make.
    val isLoadingLiveData = MediatorLiveData<Boolean>()
    val throwableLiveData = MediatorLiveData<Throwable>()

    val herosListLiveData = MediatorLiveData<List<HeroEntity>>()
    val heroListSearchQueryLiveData = MediatorLiveData<Pair<String,List<HeroEntity>>>()


    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
    }

    fun loadHeros() {
        isLoadingLiveData.value = true
        disposables.add(HerosRepository
                .getRepositories()
                .subscribe({ result ->
                    isLoadingLiveData.value = false
                    herosListLiveData.value = result
                },
                        { error ->

                            isLoadingLiveData.value = false
                            throwableLiveData.value = error
                        })
        )
    }

    fun getHero(hero : String) {
        isLoadingLiveData.value = true
        disposables.add(HerosRepository
                .getHeroByLocalName(hero)
                .subscribe({ result ->
                    isLoadingLiveData.value = false
                    heroListSearchQueryLiveData.value = Pair(hero, result)
                },
                        { error ->
                            isLoadingLiveData.value = false
                            throwableLiveData.value = error
                        })
        )
    }


}