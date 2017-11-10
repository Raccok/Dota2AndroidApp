package github.com.rhacco.dota2androidapp.utilities

import android.content.Context
import android.preference.PreferenceManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SharedPreferencesHelper(context: Context) {
    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val mSimpleDataFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

    fun getFavoriteHero(): String = defaultSharedPreferences.getString(FAV_HERO, "")

    fun setFavoriteHero(hero: String) = defaultSharedPreferences.edit().putString(FAV_HERO, hero).apply()

    fun checkProPlayersValid(): Boolean {
        val validDateString = defaultSharedPreferences.getString(PRO_PLAYERS_VALID_DATE, "")
        val validDateCal = Calendar.getInstance(Locale.ENGLISH)
        try {
            validDateCal.time = mSimpleDataFormat.parse(validDateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
        if (Calendar.getInstance(Locale.ENGLISH).after(validDateCal))
            return false
        return true
    }

    fun setProPlayersValidDate(validDate: String) =
            defaultSharedPreferences.edit().putString(PRO_PLAYERS_VALID_DATE, validDate).apply()

    companion object {
        private const val FAV_HERO = "fav_hero"
        private const val PRO_PLAYERS_VALID_DATE = "pro_players_valid_date"
    }
}