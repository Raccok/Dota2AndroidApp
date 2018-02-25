package github.com.rhacco.dotascoop.fragments

import android.os.Bundle
import android.preference.PreferenceFragment
import github.com.rhacco.dotascoop.R

class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}