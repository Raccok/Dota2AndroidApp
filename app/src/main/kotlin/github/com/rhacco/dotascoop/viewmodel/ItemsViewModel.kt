package github.com.rhacco.dotascoop.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import github.com.rhacco.dotascoop.sources.repos.CustomAPIRepository
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
                            Log.d(App.instance.getString(R.string.log_target_debug),
                                    "Failed to fetch items: " + error)
                        }
                ))
    }
}