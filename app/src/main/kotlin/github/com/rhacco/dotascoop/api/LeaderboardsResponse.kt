package github.com.rhacco.dotascoop.api

object LeaderboardsResponse {
    data class Entry(val rank: Int, val name: String,
                     val new_in_top_100: Boolean?, val last_rank: Int?)
}