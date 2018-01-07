package github.com.rhacco.dota2androidapp.utilities

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {
    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
    }
}