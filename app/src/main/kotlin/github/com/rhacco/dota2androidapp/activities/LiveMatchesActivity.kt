package github.com.rhacco.dota2androidapp.activities

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.lists.LiveMatchesAdapter
import github.com.rhacco.dota2androidapp.lists.LiveMatchesItemData
import github.com.rhacco.dota2androidapp.viewmodel.MatchesViewModel
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : BaseLifecycleActivity<MatchesViewModel>() {
    override val mViewModelClass = MatchesViewModel::class.java
    private lateinit var mAdapter: LiveMatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_matches)
        super.initNavigationDrawer(drawer_layout)

        mAdapter = LiveMatchesAdapter(this)
        live_matches_list.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        live_matches_list.layoutManager = layoutManager
        live_matches_list.addItemDecoration(
                DividerItemDecoration(live_matches_list.context, layoutManager.orientation))
        swipe_refresh_layout.setOnRefreshListener {
            mViewModel.getLiveMatches()
            swipe_refresh_layout.isRefreshing = false
        }
        observeLiveData()
        mViewModel.getLiveMatches()
    }

    override fun observeLiveData() {
        mViewModel.mLiveMatchesQuery.observe(this, Observer<List<LiveMatchesItemData>> {
            it?.let { newLiveMatches -> mAdapter.update(newLiveMatches) }
        })
        mViewModel.mHeroNamesQuery.observe(this, Observer<Pair<Long, List<String>>> {
            it?.let { (matchId, heroNames) -> mAdapter.setHeroNames(matchId, heroNames) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.button_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.button_info) {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setMessage(getString(R.string.info_live_matches))
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_ok),
                    { dialog, _ -> dialog.dismiss() })
            alertDialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}