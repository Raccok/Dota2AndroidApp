package github.com.rhacco.dota2androidapp.sources.databases.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "heroes")
data class HeroEntity(@PrimaryKey val localized_name: String, val id: Int)