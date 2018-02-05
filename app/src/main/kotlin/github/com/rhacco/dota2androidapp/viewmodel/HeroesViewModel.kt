package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.sources.databases.entities.HeroEntity
import github.com.rhacco.dota2androidapp.sources.repos.CustomAPIRepository
import io.reactivex.disposables.CompositeDisposable

class HeroesViewModel(application: Application) : AndroidViewModel(application) {
    private val mDisposables = CompositeDisposable()
    val mGetHeroes = MediatorLiveData<List<HeroEntity>>()

    override fun onCleared() = mDisposables.clear()

    fun getHeroes() {
        mDisposables.add(CustomAPIRepository.getHeroes()
                .subscribe(
                        { result -> mGetHeroes.value = result },
                        { error ->
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch heroes: " + error)
                        }
                ))
    }
}