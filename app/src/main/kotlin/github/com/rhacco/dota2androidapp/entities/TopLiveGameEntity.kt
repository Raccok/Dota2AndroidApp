package github.com.rhacco.dota2androidapp.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "game_list")
data class TopLiveGameEntity(@PrimaryKey val server_steam_id: Long?)
