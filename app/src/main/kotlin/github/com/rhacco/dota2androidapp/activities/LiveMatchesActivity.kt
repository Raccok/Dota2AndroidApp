package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.RealtimeStatsResponse
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
        observeLiveData()
        mViewModel.getTopLiveGames()
    }

    private fun updateLiveMatches(list: List<TopLiveGamesResponse.Game>) {
        for (game in list)
            mViewModel.getRealtimeStats(game.average_mmr, game.server_steam_id)
    }

    private fun updateListEntry(averageMMR: Int, result: RealtimeStatsResponse.Result) {
        if (averageMMR > 0)
            mListAdapter.mTitles.add("Ranked Match (Average MMR " + averageMMR +
                    ", Match ID " + result.match.matchid + ")")
        else
            mListAdapter.mTitles.add("Tournament Match (Match ID " +
                    result.match.matchid + ")")
        if (result.teams.size == 2) {
            var playersRadiant = "Radiant players:"
            result.teams[0].players
                    .asSequence()
                    .map {
                        if (it.name.length > 20)
                            it.name.substring(0, 19) + "..."
                        else
                            it.name
                    }
                    .forEach { playersRadiant = playersRadiant + "\n" + it }
            mListAdapter.mPlayersRadiant.add(playersRadiant)
            var playersDire = "Dire players:"
            result.teams[1].players
                    .asSequence()
                    .map {
                        if (it.name.length > 20)
                            it.name.substring(0, 19) + "..."
                        else
                            it.name
                    }
                    .forEach { playersDire = playersDire + "\n" + it }
            mListAdapter.mPlayersDire.add(playersDire)
            mListAdapter.notifyDataSetChanged()
        } else {
            Log.d(getString(R.string.log_msg_debug), "GetRealtimeStats returned " +
                    result.teams.size + " teams")
        }
    }

    override fun observeLiveData() {
        mViewModel.mTopLiveGamesQueryLiveData.observe(this, Observer<List<TopLiveGamesResponse.Game>> {
            it?.let { result -> updateLiveMatches(result) }
        })
        mViewModel.mRealtimeStatsQueryLiveData.observe(this, Observer<Pair<Int, RealtimeStatsResponse.Result>> {
            it?.let { (averageMMR, result) -> updateListEntry(averageMMR, result) }
        })
    }
}
