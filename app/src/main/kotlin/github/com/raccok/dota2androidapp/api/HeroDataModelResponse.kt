package github.com.raccok.dota2androidapp.api

import github.com.raccok.dota2androidapp.entities.HeroEntity

object HeroDataModelResponse {
    data class Result(val result: Heroes)
    data class Heroes(private val heroes: List<HeroEntity>) {
        fun herosList(): List<HeroEntity> {
            return heroes
        }
    }
    data class HeroData(val name: String, val id: Int, val localized_name: String)
}
