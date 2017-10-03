package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.entities.HeroEntity
import io.reactivex.Single

interface HeroesDataSource {
    fun getHeroes(): Single<List<HeroEntity>>
    fun getHeroByLocalizedName(hero: String): Single<List<HeroEntity>>
    fun saveHeroes(list: List<HeroEntity>) = Unit
}