package github.com.rhacco.dota2androidapp.api

object RealtimeStatsResponse {
    data class Result(val match: Match, val teams: List<Team>?)
    data class Match(val server_steam_id: Long, val matchid: Long)
    data class Team(val team_name: String, val players: List<Player>?)
    data class Player(val accountid: Long, val name: String, val heroid: Int)
}
