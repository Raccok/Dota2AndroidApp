package github.com.rhacco.dota2androidapp.utilities

import android.content.Context
import android.preference.PreferenceManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SharedPreferencesHelper(context: Context) {
    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val mSimpleDataFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

    fun getIsFirstAppStart() = defaultSharedPreferences.getBoolean(IS_FIRST_APP_START, true)

    fun setIsFirstAppStart() = defaultSharedPreferences.edit().putBoolean(IS_FIRST_APP_START, false).apply()

    fun getHeroesValid(): Boolean = !(dateNow().after(getDate(HEROES_VALID)))

    fun setHeroesValid() {
        val validDate = dateNow()
        validDate.add(Calendar.DATE, 14)
        defaultSharedPreferences.edit().putString(HEROES_VALID, validDate.time.toString()).apply()
    }

    fun getLeaderboardValid(region: String): Boolean =
            !(dateNow().after(getDate(leaderboardValidDateKey(region))))

    fun setLeaderboardValid(region: String) {
        val validDate = dateNow()
        validDate.add(Calendar.DATE, 1)
        defaultSharedPreferences.edit().putString(
                leaderboardValidDateKey(region), validDate.time.toString()).apply()
    }

    private fun leaderboardValidDateKey(region: String) = "leaderboard_" + region + "_valid"

    private fun dateNow(): Calendar = Calendar.getInstance(Locale.ENGLISH)

    private fun getDate(key: String): Calendar {
        val dateString = defaultSharedPreferences.getString(key, "")
        val date = dateNow()
        try {
            date.time = mSimpleDataFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            val invalidDate = dateNow()
            invalidDate.add(Calendar.YEAR, 100)
            return invalidDate
        }
        return date
    }

    companion object {
        private const val IS_FIRST_APP_START = "is_first_app_start"
        private const val HEROES_VALID = "heroes_valid"
    }
}