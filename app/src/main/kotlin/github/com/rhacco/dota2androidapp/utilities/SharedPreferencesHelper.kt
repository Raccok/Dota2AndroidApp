package github.com.rhacco.dota2androidapp.utilities

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {
    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getIsFirstAppStart() = defaultSharedPreferences.getBoolean(IS_FIRST_APP_START, true)

    fun setIsFirstAppStart() = defaultSharedPreferences.edit().putBoolean(IS_FIRST_APP_START, false).apply()

    companion object {
        private const val IS_FIRST_APP_START = "IS_FIRST_APP_START"
    }
}