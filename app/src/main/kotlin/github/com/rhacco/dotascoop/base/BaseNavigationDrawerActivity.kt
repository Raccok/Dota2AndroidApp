package github.com.rhacco.dotascoop.base

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.lists.BaseNavigationDrawerAdapter
import kotlinx.android.synthetic.main.navigation_drawer.*

abstract class BaseNavigationDrawerActivity : AppCompatActivity() {
    private lateinit var mNavigationDrawerToggle: ActionBarDrawerToggle

    // Must be called by each Activity
    protected fun initNavigationDrawer(drawerLayout: DrawerLayout) {
        mNavigationDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
                R.string.description_open_drawer, R.string.description_close_drawer)
        drawerLayout.addDrawerListener(mNavigationDrawerToggle)

        list_activities.adapter = BaseNavigationDrawerAdapter(this)
        val layoutManager = LinearLayoutManager(this)
        list_activities.layoutManager = layoutManager
        list_activities.addItemDecoration(
                DividerItemDecoration(list_activities.context, layoutManager.orientation))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mNavigationDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mNavigationDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mNavigationDrawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
}
