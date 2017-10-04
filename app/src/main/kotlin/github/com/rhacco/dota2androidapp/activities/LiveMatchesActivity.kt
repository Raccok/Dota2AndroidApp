package github.com.rhacco.dota2androidapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.sources.repos.matches.RealtimeStatsRepository
import github.com.rhacco.dota2androidapp.sources.repos.matches.TopLiveGamesRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : AppCompatActivity() {
    private val mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_matches)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        updateTopLiveGamesDisplay()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }

    private fun updateTopLiveGamesDisplay() {
        testText.text = "Match IDs of current top live games:"
        mDisposables.add(TopLiveGamesRepository.getTopLiveGames()
                .subscribe(
                        { result ->
                            for (entry in result)
                                entry.server_steam_id?.let { updateMatchIdsDisplay(it) }
                        },
                        { error ->
                            Log.d(getString(R.string.log_msg_debug),
                                    "Failed to update top live games: " + error)
                        }
                ))
    }

    private fun updateMatchIdsDisplay(serverSteamId: Long) {
        mDisposables.add(RealtimeStatsRepository.getRealtimeStats(serverSteamId)
                .subscribe(
                        { result -> testText.text = testText.text as String + "\n" + result.matchid },
                        { error ->
                            Log.d(getString(R.string.log_msg_debug),
                                    "Failed to update match IDs: " + error)
                        }
                ))
    }
}
