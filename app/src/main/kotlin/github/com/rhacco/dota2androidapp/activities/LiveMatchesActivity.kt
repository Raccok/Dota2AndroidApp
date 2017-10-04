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
                            for (game in result.game_list) {
                                updateMatchIdsDisplay(game.server_steam_id)
                                testText.text = testText.text as String + "\n" + game.average_mmr
                            }
                        },
                        { error ->
                            Log.d(getString(R.string.log_msg_debug),
                                    "Failed to update top live games: " + error)
                        }
                ))
    }

    // TODO pass on display item to set attributes on
    private fun updateMatchIdsDisplay(serverSteamId: Long) {
        mDisposables.add(RealtimeStatsRepository.getRealtimeStats(serverSteamId)
                .subscribe(
                        { result ->
                            testText.text = testText.text as String + "\n" + result.match.matchid
                            testText.text = testText.text as String + "\nRadiant\n"
                            for (player in result.teams[0].players)
                                testText.text = testText.text as String + player.name + " "
                            testText.text = testText.text as String + "\nDire\n"
                            for (player in result.teams[1].players)
                                testText.text = testText.text as String + player.name + " "
                        },
                        { error ->
                            Log.d(getString(R.string.log_msg_debug),
                                    "Failed to update match IDs: " + error)
                        }
                ))
    }
}
