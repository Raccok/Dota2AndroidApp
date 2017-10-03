package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
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

    private fun displayTopLiveGames(list: List<TopLiveGameEntity>) {
        testText.text = "Steam server IDs of current top live games:"
        for (topLiveGame in list)
            testText.text = testText.text as String + "\n" + topLiveGame.server_steam_id
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.mTopLiveGamesQueryLiveData.observe(this, Observer<List<TopLiveGameEntity>> {
            it?.let { list -> displayTopLiveGames(list) }
        })
    }
}