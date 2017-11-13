package github.com.rhacco.dota2androidapp.api

object RealtimeStatsResponse {
    data class Result(val match: Match, val teams: List<Team>?, val graph_data: GraphData?)
    data class Match(val server_steam_id: Long, val matchid: Long, val game_time: Int)
    data class Team(val score: Int, val players: List<Player>?)
    data class Player(val accountid: Long, val name: String, val heroid: Int)
    data class GraphData(val graph_gold: List<Int>?)
}
