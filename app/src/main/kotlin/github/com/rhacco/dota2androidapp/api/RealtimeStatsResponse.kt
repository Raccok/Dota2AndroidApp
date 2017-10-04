package github.com.rhacco.dota2androidapp.api

object RealtimeStatsResponse {
    data class Result(val match: Match, val teams: List<Team>)
    data class Match(val matchid: Long)
    data class Team(val team_name: String, val players: List<Player>)
    data class Player(val name: String, val heroid: Int)
}
