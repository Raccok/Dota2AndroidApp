package github.com.rhacco.dota2androidapp.sources.databases.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(@PrimaryKey val dname: String, val id: Int, val is_recipe: Boolean?)