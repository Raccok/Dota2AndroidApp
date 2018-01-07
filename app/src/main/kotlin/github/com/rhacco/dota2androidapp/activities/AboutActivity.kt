package github.com.rhacco.dota2androidapp.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseNavigationDrawerActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseNavigationDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        super.initNavigationDrawer(drawer_layout)
        paragraph0.movementMethod = LinkMovementMethod.getInstance()
        paragraph2.movementMethod = LinkMovementMethod.getInstance()
    }
}