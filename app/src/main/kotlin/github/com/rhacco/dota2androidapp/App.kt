package github.com.rhacco.dota2androidapp

import android.app.Application
import github.com.rhacco.dota2androidapp.sources.db.DatabaseCreator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseCreator.createDb(this)
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}