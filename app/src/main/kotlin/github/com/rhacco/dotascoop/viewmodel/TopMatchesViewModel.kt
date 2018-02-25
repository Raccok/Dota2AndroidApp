package github.com.rhacco.dotascoop.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.api.TopMatchesResponse
import github.com.rhacco.dotascoop.lists.Player
import github.com.rhacco.dotascoop.lists.TopMatchesItemData
import github.com.rhacco.dotascoop.sources.repos.CustomAPIRepository
import io.reactivex.disposables.CompositeDisposable

class TopMatchesViewModel(application: Application) : AndroidViewModel(application) {
    private val mDisposables = CompositeDisposable()
    val mGetTopMatchesQuery = MediatorLiveData<List<TopMatchesItemData>>()

    override fun onCleared() = mDisposables.clear()

    fun getTopLiveMatches() {
        mDisposables.add(CustomAPIRepository.getTopLiveMatches()
                .subscribe(
                        { result -> mGetTopMatchesQuery.value = buildListItemsData(result) },
                        { error ->
                            Log.d(App.instance.getString(R.string.log_target_debug),
                                    "Failed to fetch top live matches: " + error)
                        }
                ))
    }

    fun getTopRecentMatches() {
        mDisposables.add(CustomAPIRepository.getTopRecentMatches()
                .subscribe(
                        { result -> mGetTopMatchesQuery.value = buildListItemsData(result) },
                        { error ->
                            Log.d(App.instance.getString(R.string.log_target_debug),
                                    "Failed to fetch top recent matches: " + error)
                        }
                ))
    }

    private fun buildListItemsData(remoteResult: List<TopMatchesResponse.Match>): List<TopMatchesItemData> {
        val listItemsData: MutableList<TopMatchesItemData> = mutableListOf()
        remoteResult.forEach {
            val newItemData = TopMatchesItemData()
            if (it.server_id != null)
                newItemData.serverId = it.server_id
            newItemData.matchId = it.match_id
            newItemData.isTournamentMatch = it.is_tournament_match
            if (it.spectators != null)
                newItemData.spectators = it.spectators
            if (it.is_tournament_match) {
                newItemData.teamRadiant = it.team_radiant!!
                newItemData.teamDire = it.team_dire!!
            } else {
                newItemData.teamRadiant = App.instance.getString(R.string.team_radiant)
                newItemData.teamDire = App.instance.getString(R.string.team_dire)
                newItemData.averageMMR = it.average_mmr!!
            }
            if (it.radiant_win != null) {
                if (it.radiant_win)
                    newItemData.teamRadiant += " Victory"
                else
                    newItemData.teamDire += " Victory"
            }
            newItemData.goldAdvantage = it.gold_advantage
            newItemData.radiantScore = it.radiant_score
            if (it.elapsed_time != null)
                newItemData.elapsedTime = it.elapsed_time
            else
                newItemData.elapsedTime = it.duration!!
            newItemData.direScore = it.dire_score
            it.players.forEach {
                newItemData.players.add(Player(it.current_steam_name, it.official_name, it.score_kda))
            }
            if (it.heroes != null)
                newItemData.heroes = it.heroes
            newItemData.showAdditionalInfo = App.sSharedPreferences.getExpandMatches()
            listItemsData.add(newItemData)
        }
        return listItemsData
    }
}