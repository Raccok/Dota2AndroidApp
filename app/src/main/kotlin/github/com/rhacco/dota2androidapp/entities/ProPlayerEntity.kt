package github.com.rhacco.dota2androidapp.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "proPlayers")
data class ProPlayerEntity(@PrimaryKey val account_id: Long?, val name: String?)
