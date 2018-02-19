package github.com.rhacco.dota2androidapp.sources.databases.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "heroes")
data class HeroEntity(@PrimaryKey val localized_name: String, val id: Int,
                      val primary_attr: String, val attack_type: String, val roles: List<String>,
                      val base_str: Int, val base_agi: Int, val base_int: Int,
                      val str_gain: Float, val agi_gain: Float, val int_gain: Float,
                      val base_health: Int, val base_armor: Float, val base_mana: Int,
                      val base_attack_min: Int, val base_attack_max: Int,
                      val attack_rate: Float, val attack_range: Int, val projectile_speed: Int,
                      val base_mr: Int, val move_speed: Int, val turn_rate: Float,
                      val abilities: List<Ability>)

data class Ability(val dname: String, val desc: String, val behavior: String, val dmg_type: String?,
                   val bkbpierce: String?, val attrib: List<Attribute>, val cd: String?, val mc: String?)

data class Attribute(val header: String)