package github.com.rhacco.dotascoop

import android.app.Application
import android.util.Log
import github.com.rhacco.dotascoop.sources.databases.createAppDatabase
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import github.com.rhacco.dotascoop.utilities.SharedPreferencesHelper
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {
    init {
        instance = this

        RxJavaPlugins.setErrorHandler { throwable ->
            Log.d(getString(R.string.log_target_debug),
                    "Uncaught RxJava exception: " + throwable.toString())
        }
    }

    companion object {
        lateinit var instance: App
        val sSharedPreferences by lazy { SharedPreferencesHelper(instance.applicationContext) }
        val sDatabase by lazy { createAppDatabase(instance.applicationContext) }
        lateinit var sCurrentHeroToDisplay: HeroEntity
        lateinit var sCurrentItemToDisplay: ItemEntity
    }
}