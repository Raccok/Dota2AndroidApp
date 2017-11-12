package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
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
                for (match in topLiveMatches)
                    mViewModel.getLiveMatchesItemData(match.average_mmr, match.server_steam_id)
            }
        })
        mViewModel.mLiveMatchesItemDataQuery.observe(this, Observer<LiveMatchesItemData> {
            it?.let { itemData ->
                if (mAdapter.add(itemData))
                    live_matches_list.layoutManager.scrollToPosition(0)
                else
                    mViewModel.checkMatchFinished(itemData.mMatchID)
            }
        })
        mViewModel.mCheckProPlayersQuery.observe(this, Observer<List<ProPlayerEntity>> {
            it?.let { proPlayers -> mAdapter.setOfficialNames(proPlayers) }
        })
        mViewModel.mCheckMatchFinishedQuery.observe(this, Observer<Pair<Long, Boolean>> {
            it?.let { (matchId, isFinished) ->
                if (isFinished)
                    mViewModel.removeFinishedMatch(matchId)
            }
        })
        mViewModel.mRemoveFinishedMatchQuery.observe(this, Observer<Long> {
            it?.let { matchId -> mAdapter.remove(matchId) }
        })
    }
}
