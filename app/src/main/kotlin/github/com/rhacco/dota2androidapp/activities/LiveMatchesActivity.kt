package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity
import github.com.rhacco.dota2androidapp.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : BaseLifecycleActivity<ReposViewModel>() {
    override val mViewModelClass = ReposViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_matches)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        observeLiveData()
        mViewModel.updateTopLiveGames()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.updateTopLiveGames()
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.mTopLiveGamesQueryLiveData.observe(this, Observer<List<TopLiveGameEntity>> {
            it?.let { list -> updateTopLiveGamesDisplay(list) }
        })
        mViewModel.mRealtimeStatsQueryLiveData.observe(this, Observer<RealtimeStatsEntity> {
            it?.let { entry -> updateMatchIdsDisplay(entry) }
        })
    }

    private fun updateTopLiveGamesDisplay(list: List<TopLiveGameEntity>) {
        testText.text = "Match IDs of current top live games:"
        for (topLiveGame in list)
            if (topLiveGame.server_steam_id != null)
                mViewModel.updateRealtimeStats(topLiveGame.server_steam_id)
    }

    private fun updateMatchIdsDisplay(entry: RealtimeStatsEntity) {
        testText.text = testText.text as String + "\n" + entry.match_id
    }
}