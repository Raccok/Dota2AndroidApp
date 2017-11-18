package github.com.rhacco.dota2androidapp.activities

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
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
        mViewModel.mLiveMatchesQuery.observe(this, Observer<List<TopLiveGamesResponse.Game>> {
            it?.let { topLiveMatches ->
                val matchesInListToUpdate: MutableMap<Long, LiveMatchesItemData> = mutableMapOf()
                mAdapter.getItemsData().forEach { matchesInListToUpdate[it.mServerId] = it }
                topLiveMatches.forEach {
                    mViewModel.getLiveMatchesItemData(it.team_name_radiant, it.team_name_dire,
                            it.average_mmr, it.server_steam_id)
                    matchesInListToUpdate.remove(it.server_steam_id)
                }
                matchesInListToUpdate.values.forEach {
                    mViewModel.getLiveMatchesItemData(it.mTeamRadiant, it.mTeamDire,
                            it.mAverageMMR, it.mServerId)
                    mViewModel.checkMatchFinished(it.mMatchId)
                }
            }
        })
        mViewModel.mLiveMatchesItemDataQuery.observe(this, Observer<LiveMatchesItemData> {
            it?.let { newItemData ->
                if (mAdapter.add(newItemData))
                    live_matches_list.layoutManager.scrollToPosition(0)
                else {
                    mAdapter.updateRealtimeStats(newItemData)
                    mViewModel.checkMatchFinished(newItemData.mMatchId)
                }
            }
        })
        mViewModel.mCheckProPlayersQuery.observe(this, Observer<Pair<Long, List<ProPlayerEntity>>> {
            it?.let { (matchId, proPlayers) -> mAdapter.setOfficialNames(matchId, proPlayers) }
        })
        mViewModel.mHeroesQuery.observe(this, Observer<Pair<Long, List<HeroEntity>>> {
            it?.let { (matchId, heroEntities) -> mAdapter.setHeroNames(matchId, heroEntities) }
        })
        mViewModel.mCheckMatchFinishedQuery.observe(this, Observer<Long> {
            it?.let { matchId -> mAdapter.remove(matchId) }
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
