package github.com.rhacco.dota2androidapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import github.com.rhacco.dota2androidapp.App

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent
        if (App.sSharedPreferences.getIsFirstAppStart()) {
            intent = Intent(this, AboutActivity::class.java)
            App.sSharedPreferences.setIsFirstAppStart()
        } else
            intent = Intent(this, TopMatchesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}