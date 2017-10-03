package github.com.rhacco.dota2androidapp.api

import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity

object RealtimeStatsResponse {
    data class Result(val match: RealtimeStatsEntity)
}
