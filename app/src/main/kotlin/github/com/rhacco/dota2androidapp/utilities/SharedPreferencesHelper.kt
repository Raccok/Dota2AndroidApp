package github.com.rhacco.dota2androidapp.utilities

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {
    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getFavoriteHero(): String = defaultSharedPreferences.getString(FAV_HERO, "")

    fun setFavoriteHero(hero: String) = defaultSharedPreferences.edit().putString(FAV_HERO, hero).apply()

    companion object {
        private const val FAV_HERO = "fav_hero"
    }
}