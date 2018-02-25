package github.com.rhacco.dotascoop.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.base.BaseNavigationDrawerActivity
import github.com.rhacco.dotascoop.utilities.checkPermissions
import github.com.rhacco.dotascoop.utilities.stripUnderlines
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseNavigationDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        super.initNavigationDrawer(drawer_layout)
        paragraph0.movementMethod = LinkMovementMethod.getInstance()
        stripUnderlines(paragraph0)
        paragraph2.movementMethod = LinkMovementMethod.getInstance()
        stripUnderlines(paragraph2)
        checkPermissions(applicationContext)
    }
}