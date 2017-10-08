package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.lists.LiveMatchesListAdapter
import github.com.rhacco.dota2androidapp.viewmodel.MatchesViewModel
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : BaseLifecycleActivity<MatchesViewModel>() {
    override val mViewModelClass = MatchesViewModel::class.java
    private lateinit var mListAdapter: LiveMatchesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_matches)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        mListAdapter = LiveMatchesListAdapter(this)
        listView.adapter = mListAdapter
        swipeRefreshLayout.setOnRefreshListener {
            mListAdapter.mListItemsData.clear()
            mViewModel.clearLiveMatches()
            mViewModel.clearLiveMatchesListData()
            mViewModel.getLiveMatches()
            swipeRefreshLayout.isRefreshing = false
        }
        observeLiveData()
        if (savedInstanceState == null)
            mViewModel.getLiveMatches()
    }

    private fun updateLiveMatches(list: List<TopLiveGamesResponse.Game>) {
        for (game in list)
            mViewModel.getLiveMatchListData(game.average_mmr, game.server_steam_id)
    }

    // Add tournament matches first, then sort by average MMR (descending)
    private fun updateLiveMatchesListData(listItemData: LiveMatchesListAdapter.ListItemData) {
        if (listItemData.mAverageMMR < 1) {
            mListAdapter.mListItemsData.add(0, listItemData)
            return
        }
        var index = 0
        for (item in mListAdapter.mListItemsData) {
            if (item.mAverageMMR < 1 || item.mAverageMMR >= listItemData.mAverageMMR) {
                ++index
                continue
            }
            mListAdapter.mListItemsData.add(index, listItemData)
            return
        }
        mListAdapter.mListItemsData.add(listItemData)
    }

    override fun observeLiveData() {
        mViewModel.mLiveMatchesQueryLiveData.observe(this, Observer<List<TopLiveGamesResponse.Game>> {
            it?.let { result -> updateLiveMatches(result) }
        })
        mViewModel.mLiveMatchListDataQueryLiveData
                .observe(this, Observer<LiveMatchesListAdapter.ListItemData> {
                    it?.let { listItemData ->
                        updateLiveMatchesListData(listItemData)
                        mListAdapter.notifyDataSetChanged()
                    }
        })
    }
}
