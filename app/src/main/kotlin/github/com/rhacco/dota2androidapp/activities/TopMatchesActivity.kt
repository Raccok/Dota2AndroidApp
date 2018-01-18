package github.com.rhacco.dota2androidapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseNavigationDrawerActivity
import github.com.rhacco.dota2androidapp.fragments.TopMatchesFragment
import github.com.rhacco.dota2androidapp.utilities.appIsMissingPermissions
import kotlinx.android.synthetic.main.view_pager.*

class TopMatchesActivity : BaseNavigationDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager)
        super.initNavigationDrawer(drawer_layout)

        if (appIsMissingPermissions(applicationContext))
            return

        view_pager.adapter = CustomPagerAdapter(supportFragmentManager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.button_info, menu)
        return true
    }

    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.button_info) {
            val alertDialog = AlertDialog.Builder(
                    ContextThemeWrapper(this, R.style.AlertDialogTheme)).create()
            val view = layoutInflater.inflate(R.layout.info_top_matches, null)
            alertDialog.setView(view)
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_ok),
                    { dialog, _ -> dialog.dismiss() })
            alertDialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private class CustomPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment? {
            val fragment = TopMatchesFragment()
            val bundle = Bundle()
            bundle.putInt("tab_position", position)
            fragment.arguments = bundle
            return fragment
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence =
                when (position) {
                    0 -> "Live"
                    else -> "Recent"
                }
    }
}