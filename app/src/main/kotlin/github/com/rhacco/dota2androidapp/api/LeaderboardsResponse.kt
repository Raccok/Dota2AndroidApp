package github.com.rhacco.dota2androidapp.api

object LeaderboardsResponse {
    data class Entry(val rank: Int, val name: String, val rank_change: String?)
}