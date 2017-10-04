package github.com.rhacco.dota2androidapp.api

object TopLiveGamesResponse {
    data class Result(val game_list: List<Game>)
    data class Game(val server_steam_id: Long, val average_mmr: Int)
}
