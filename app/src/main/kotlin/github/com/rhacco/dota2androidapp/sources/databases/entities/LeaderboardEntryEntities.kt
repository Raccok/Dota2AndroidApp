package github.com.rhacco.dota2androidapp.sources.databases.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Suppress("PropertyName")
interface LeaderboardEntryEntity {
    val rank: Int
    val name: String
    val last_rank: Int?
}

@Entity(tableName = "leaderboard_americas")
data class LeaderboardEntryAmericas(
        @PrimaryKey override val rank: Int, override val name: String, override val last_rank: Int?) :
        LeaderboardEntryEntity

@Entity(tableName = "leaderboard_europe")
data class LeaderboardEntryEurope(
        @PrimaryKey override val rank: Int, override val name: String, override val last_rank: Int?) :
        LeaderboardEntryEntity

@Entity(tableName = "leaderboard_se_asia")
data class LeaderboardEntrySEAsia(
        @PrimaryKey override val rank: Int, override val name: String, override val last_rank: Int?) :
        LeaderboardEntryEntity

@Entity(tableName = "leaderboard_china")
data class LeaderboardEntryChina(
        @PrimaryKey override val rank: Int, override val name: String, override val last_rank: Int?) :
        LeaderboardEntryEntity
