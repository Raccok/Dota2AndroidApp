package github.com.rhacco.dota2androidapp.api

import github.com.rhacco.dota2androidapp.entities.HeroEntity

object HeroesResponse {
    data class Result(val result: Heroes)
    data class Heroes(private val heroes: List<HeroEntity>) {
        fun heroesList(): List<HeroEntity> = heroes
    }
}
