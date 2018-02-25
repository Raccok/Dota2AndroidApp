package github.com.rhacco.dotascoop.activities

import android.os.Bundle
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.base.BaseNavigationDrawerActivity
import github.com.rhacco.dotascoop.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseNavigationDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        super.initNavigationDrawer(drawer_layout)
        fragmentManager.beginTransaction()
                .replace(R.id.dummy, SettingsFragment())
                .commit()
    }
}