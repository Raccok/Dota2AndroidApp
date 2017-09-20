package github.com.raccok.dota2androidapp.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "heros")
data class HeroEntity(
        @PrimaryKey var id: Int?,
        val name: String?, val localized_name: String?)