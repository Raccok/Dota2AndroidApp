package github.com.rhacco.dota2androidapp.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "realtime_stats")
data class RealtimeStatsEntity(@PrimaryKey val server_steam_id: Long?, val matchid: Long?)
