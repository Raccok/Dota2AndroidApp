package github.com.rhacco.dotascoop.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.base.BaseNavigationDrawerActivity
import github.com.rhacco.dotascoop.fragments.TopMatchesFragment
import github.com.rhacco.dotascoop.utilities.checkPermissions
import kotlinx.android.synthetic.main.view_pager.*

class TopMatchesActivity : BaseNavigationDrawerActivity() {
    private var mCurrentTabPosition = 0
    private lateinit var mAdapter: CustomPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager)
        super.initNavigationDrawer(drawer_layout)
        mAdapter = CustomPagerAdapter(supportFragmentManager)
        view_pager.adapter = mAdapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                mAdapter.mAllFragments[mCurrentTabPosition].showAllEntries()
                mCurrentTabPosition = position
            }

            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
        })
        handleIntent(intent)
        checkPermissions(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view, menu)
        menuInflater.inflate(R.menu.button_info, menu)
        val searchMenuItem = menu?.findItem(R.id.search)
        val searchView = searchMenuItem?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setPadding(-30, 15, 20, 15)
        searchView.setIconifiedByDefault(false)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnCloseListener { handleSearchClosed() }
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean = handleSearchClosed()

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean = true
        })
        return true
    }

    override fun onNewIntent(intent: Intent) = handleIntent(intent)

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_SEARCH)
            mAdapter.mAllFragments[mCurrentTabPosition].handleSearchQuery(
                    intent.getStringExtra(SearchManager.QUERY))
    }

    private fun handleSearchClosed(): Boolean {
        mAdapter.mAllFragments[mCurrentTabPosition].showAllEntries()
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
        val mAllFragments: MutableList<TopMatchesFragment> = mutableListOf()

        override fun getItem(position: Int): Fragment? {
            val fragment = TopMatchesFragment()
            val bundle = Bundle()
            bundle.putInt("tab_position", position)
            fragment.arguments = bundle
            mAllFragments.add(fragment)
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