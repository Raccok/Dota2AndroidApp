package github.com.rhacco.dota2androidapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.sources.databases.entities.ItemEntity
import github.com.rhacco.dota2androidapp.sources.repos.CustomAPIRepository
import io.reactivex.disposables.CompositeDisposable

class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val mDisposables = CompositeDisposable()
    val mGetItems = MediatorLiveData<List<ItemEntity>>()

    override fun onCleared() = mDisposables.clear()

    fun getItems() {
        mDisposables.add(CustomAPIRepository.getItems()
                .subscribe(
                        { result -> mGetItems.value = result },
                        { error ->
                            Log.d(App.instance.getString(R.string.log_msg_debug),
                                    "Failed to fetch items: " + error)
                        }
                ))
    }
}