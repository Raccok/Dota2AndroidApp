package github.com.rhacco.dotascoop

import android.app.Application
import github.com.rhacco.dotascoop.sources.databases.createAppDatabase
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import github.com.rhacco.dotascoop.utilities.SharedPreferencesHelper

class App : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
        val sSharedPreferences by lazy { SharedPreferencesHelper(instance.applicationContext) }
        val sDatabase by lazy { createAppDatabase(instance.applicationContext) }
        lateinit var sCurrentHeroToDisplay: HeroEntity
        lateinit var sCurrentItemToDisplay: ItemEntity
    }
}