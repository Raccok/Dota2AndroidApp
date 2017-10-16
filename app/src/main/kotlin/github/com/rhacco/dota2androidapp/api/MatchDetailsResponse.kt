package github.com.rhacco.dota2androidapp.api

object MatchDetailsResponse {
    data class Result(val result: Values)
    data class Values(val error: String?)
}
