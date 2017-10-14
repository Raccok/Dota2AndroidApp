package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.lists.LiveMatchItemData
import github.com.rhacco.dota2androidapp.lists.LiveMatchesAdapter
import github.com.rhacco.dota2androidapp.viewmodel.MatchesViewModel
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : BaseLifecycleActivity<MatchesViewModel>() {
    override val mViewModelClass = MatchesViewModel::class.java
    private lateinit var mAdapter: LiveMatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_matches)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        mAdapter = LiveMatchesAdapter(this)
        recycler_view.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(
                DividerItemDecoration(recycler_view.context, layoutManager.orientation))
        observeLiveData()
        mViewModel.getLiveMatches()
    }

    override fun observeLiveData() {
        mViewModel.mLiveMatchesQuery.observe(this, Observer<List<TopLiveGamesResponse.Game>> {
            it?.let { list ->
                for (game in list)
                    mViewModel.getLiveMatchItemData(game.average_mmr, game.server_steam_id)
            }
        })
        mViewModel.mLiveMatchItemDataQuery.observe(this, Observer<LiveMatchItemData> {
            it?.let { itemData -> mAdapter.add(itemData) }
        })
    }
}
