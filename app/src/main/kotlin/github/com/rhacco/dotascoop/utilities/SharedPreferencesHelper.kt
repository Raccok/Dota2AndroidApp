package github.com.rhacco.dotascoop.utilities

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {
    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getIsFirstAppStart() = defaultSharedPreferences.getBoolean(IS_FIRST_APP_START, true)

    fun setIsFirstAppStart() =
            defaultSharedPreferences.edit().putBoolean(IS_FIRST_APP_START, false).apply()

    fun getStartScreen() = defaultSharedPreferences.getString("pref_start_screen", "")!!

    fun getExpandMatches() = defaultSharedPreferences.getBoolean("pref_expand_matches", false)

    fun getDefaultLeaderboard() =
            defaultSharedPreferences.getString("pref_default_leaderboard", "")!!

    fun getHeroesNeedUpdate(remoteLastUpdate: Float): Boolean =
            remoteLastUpdate > defaultSharedPreferences.getLong(HEROES_LAST_UPDATE, 0)

    fun setHeroesLastUpdate() = defaultSharedPreferences.edit()
            .putLong(HEROES_LAST_UPDATE, currentUnixTimestamp()).apply()

    fun getItemsNeedUpdate(remoteLastUpdate: Float): Boolean =
            remoteLastUpdate > defaultSharedPreferences.getLong(ITEMS_LAST_UPDATE, 0)

    fun setItemsLastUpdate() = defaultSharedPreferences.edit()
            .putLong(ITEMS_LAST_UPDATE, currentUnixTimestamp()).apply()

    fun getLeaderboardNeedsUpdate(region: String, remoteLastUpdate: Float): Boolean =
            remoteLastUpdate > defaultSharedPreferences.getLong(leaderboardLastUpdateKey(region), 0)

    fun setLeaderboardLastUpdate(region: String) = defaultSharedPreferences.edit()
            .putLong(leaderboardLastUpdateKey(region), currentUnixTimestamp()).apply()

    private fun leaderboardLastUpdateKey(region: String) = "leaderboard_" + region + "_last_update"

    companion object {
        private const val IS_FIRST_APP_START = "is_first_app_start"
        private const val HEROES_LAST_UPDATE = "heroes_last_update"
        private const val ITEMS_LAST_UPDATE = "items_last_update"
    }
}