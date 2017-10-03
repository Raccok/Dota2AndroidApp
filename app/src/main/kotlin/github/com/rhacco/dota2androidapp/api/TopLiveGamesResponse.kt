package github.com.rhacco.dota2androidapp.api

import github.com.rhacco.dota2androidapp.entities.TopLiveGameEntity

object TopLiveGamesResponse {
    data class Result(private val game_list: List<TopLiveGameEntity>) {
        fun topLiveGamesList(): List<TopLiveGameEntity> = game_list
    }
}
