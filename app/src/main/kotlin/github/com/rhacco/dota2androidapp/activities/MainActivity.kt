package github.com.rhacco.dota2androidapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import github.com.rhacco.dota2androidapp.utilities.SharedPreferencesHelper

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent
        if (SharedPreferencesHelper(applicationContext).getIsFirstAppStart()) {
            intent = Intent(this, AboutActivity::class.java)
            SharedPreferencesHelper(applicationContext).setIsFirstAppStart()
        } else
            intent = Intent(this, TopMatchesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}