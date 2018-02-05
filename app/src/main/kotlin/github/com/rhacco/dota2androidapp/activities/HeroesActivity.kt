package github.com.rhacco.dota2androidapp.activities

import android.os.Bundle
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseNavigationDrawerActivity
import kotlinx.android.synthetic.main.activity_heroes.*

class HeroesActivity : BaseNavigationDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heroes)
        super.initNavigationDrawer(drawer_layout)
    }
}