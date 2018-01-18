package github.com.rhacco.dota2androidapp.api

object LeaderboardsResponse {
    data class Result(val leaderboard: List<Entry>)
    data class Entry(val name: String, val team_tag: String?)
}