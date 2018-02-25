package github.com.rhacco.dotascoop.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        val intent: Intent
        if (App.sSharedPreferences.getIsFirstAppStart()) {
            intent = Intent(this, AboutActivity::class.java)
            App.sSharedPreferences.setIsFirstAppStart()
        } else
            when (App.sSharedPreferences.getStartScreen()) {
                getString(R.string.activity_top_matches) ->
                    intent = Intent(this, TopMatchesActivity::class.java)
                getString(R.string.activity_heroes) ->
                    intent = Intent(this, HeroesActivity::class.java)
                getString(R.string.activity_items) ->
                    intent = Intent(this, ItemsActivity::class.java)
                getString(R.string.activity_leaderboards) ->
                    intent = Intent(this, LeaderboardsActivity::class.java)
                getString(R.string.activity_settings) ->
                    intent = Intent(this, SettingsActivity::class.java)
                else ->
                    intent = Intent(this, AboutActivity::class.java)
            }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}