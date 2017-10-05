package github.com.rhacco.dota2androidapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.lists.LiveMatchesListAdapter
import github.com.rhacco.dota2androidapp.sources.remote.getDota2OfficialAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : AppCompatActivity() {
    private lateinit var mListAdapter: LiveMatchesListAdapter
    private val mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_matches)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        mListAdapter = LiveMatchesListAdapter(this)
        listView.adapter = mListAdapter
        updateLiveMatches()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }

    private fun updateLiveMatches() {
        val disposable = getDota2OfficialAPIService()
                ?.fetchTopLiveGames(getString(R.string.api_key), 0)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { result ->
                            for (game in result.game_list)
                                updateListEntry(game.server_steam_id, game.average_mmr)
                        },
                        { error ->
                            Log.d(getString(R.string.log_msg_debug),
                                    "Failed to update top live games: " + error)
                        }
                )
        if (disposable != null)
            mDisposables.add(disposable)
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
                                    "Failed to update match IDs: " + error)
                        }
                )
        if (disposable != null)
            mDisposables.add(disposable)
    }
}
