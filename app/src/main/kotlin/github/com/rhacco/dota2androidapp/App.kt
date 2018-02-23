package github.com.rhacco.dota2androidapp

import android.app.Application
import github.com.rhacco.dota2androidapp.sources.databases.createAppDatabase
import github.com.rhacco.dota2androidapp.sources.databases.entities.HeroEntity
import github.com.rhacco.dota2androidapp.sources.databases.entities.ItemEntity
import github.com.rhacco.dota2androidapp.utilities.SharedPreferencesHelper

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