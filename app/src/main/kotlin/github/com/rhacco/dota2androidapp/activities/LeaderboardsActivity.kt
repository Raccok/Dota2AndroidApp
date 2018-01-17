package github.com.rhacco.dota2androidapp.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseNavigationDrawerActivity
import github.com.rhacco.dota2androidapp.fragments.LeaderboardsFragment
import kotlinx.android.synthetic.main.view_pager.*

class LeaderboardsActivity : BaseNavigationDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager)
        super.initNavigationDrawer(drawer_layout)
        view_pager.adapter = CustomPagerAdapter(supportFragmentManager)
    }

    private class CustomPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment? {
            val fragment = LeaderboardsFragment()
            val bundle = Bundle()
            bundle.putInt("tab_position", position)
            fragment.arguments = bundle
            return fragment
        }

        override fun getCount(): Int = 4

        override fun getPageTitle(position: Int): CharSequence =
                when (position) {
                    0 -> "Americas"
                    1 -> "Europe"
                    2 -> "SE Asia"
                    else -> "China"
                }
    }
}