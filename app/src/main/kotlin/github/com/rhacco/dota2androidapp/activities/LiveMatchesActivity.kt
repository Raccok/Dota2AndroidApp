package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.lists.LiveMatchesListAdapter
import github.com.rhacco.dota2androidapp.sources.remote.getDota2OfficialAPIService
import github.com.rhacco.dota2androidapp.viewmodel.TopLiveGamesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : BaseLifecycleActivity<TopLiveGamesViewModel>() {
    override val mViewModelClass = TopLiveGamesViewModel::class.java
    private lateinit var mListAdapter: LiveMatchesListAdapter
    private val mDisposables = CompositeDisposable()

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
            updateListEntry(game.server_steam_id, game.average_mmr)
    }

    private fun updateListEntry(serverSteamId: Long, averageMMR: Int) {
        val disposable = getDota2OfficialAPIService()
                ?.fetchRealtimeStats(getString(R.string.api_key), serverSteamId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { result ->
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
                        },
                        { error ->
                            Log.d(getString(R.string.log_msg_debug),
                                    "Failed to update realtime stats (one live match): " + error)
                        }
                )
        if (disposable != null)
            mDisposables.add(disposable)
    }

    override fun observeLiveData() {
        mViewModel.mTopLiveGamesQueryLiveData.observe(this, Observer<List<TopLiveGamesResponse.Game>> {
            it?.let { result -> updateLiveMatches(result) }
        })
    }
}
