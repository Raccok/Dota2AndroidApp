package github.com.raccok.dota2androidapp.sources.repos

import github.com.raccok.dota2androidapp.entities.HeroEntity
import io.reactivex.Single

interface HeroesDataSource {
    fun getRepositories(): Single<List<HeroEntity>>
    fun getHeroByLocalName(hero: String): Single<List<HeroEntity>>
    fun saveRepositories(list: List<HeroEntity>) = Unit
}