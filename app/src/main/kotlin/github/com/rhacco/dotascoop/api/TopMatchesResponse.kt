package github.com.rhacco.dotascoop.api

object TopMatchesResponse {
    data class Match(val server_id: Long?, val match_id: Long, val is_tournament_match: Boolean,
                     val spectators: Int?, val radiant_win: Boolean?,
                     val team_radiant: String?, val team_dire: String?, val average_mmr: Int?,
                     val gold_advantage: Int, val elapsed_time: Int?, val duration: Int?,
                     val radiant_score: Int, val dire_score: Int,
                     val players: List<Player>, val hero_ids: List<Int>?, val hero_names: String?)

    data class Player(val current_steam_name: String, val official_name: String,
                      val score_kda: String)
}