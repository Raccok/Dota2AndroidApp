package github.com.raccok.dota2androidapp.api

import github.com.raccok.dota2androidapp.entities.HeroEntity

object HeroDataModelResponse {
    data class Result(val result: Heroes)
    data class Heroes(private val heroes: List<HeroEntity>) {
        fun heroesList(): List<HeroEntity> = heroes
    }
}
