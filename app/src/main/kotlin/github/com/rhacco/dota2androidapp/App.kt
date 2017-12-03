package github.com.rhacco.dota2androidapp

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // DatabaseCreator.createDb(this) TODO: not used currently
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}