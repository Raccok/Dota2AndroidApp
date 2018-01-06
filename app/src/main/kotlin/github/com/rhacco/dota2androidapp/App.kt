package github.com.rhacco.dota2androidapp

import android.app.Application

class App : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}