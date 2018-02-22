package github.com.rhacco.dota2androidapp.sources.databases.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(@PrimaryKey val dname: String, val id: Int, val cost: Int,
                      val attrib: List<ItemAttribute>, val mc: Int?, val cd: Int?,
                      val desc: String, val lore: String, val notes: String)

data class ItemAttribute(val header: String, val value: String, val footer: String?)