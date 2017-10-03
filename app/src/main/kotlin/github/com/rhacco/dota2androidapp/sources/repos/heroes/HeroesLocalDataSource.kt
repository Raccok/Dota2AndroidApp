package github.com.rhacco.dota2androidapp.sources.repos.heroes

import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.sources.db.DatabaseCreator
import io.reactivex.Single

object HeroesLocalDataSource : HeroesDataSource {
    private val mHeroesDao = DatabaseCreator.mDatabase.heroesDao()

    override fun getHeroes(): Single<List<HeroEntity>> =
            mHeroesDao.loadAllHeroes()
                    .firstOrError()
                    .doOnSuccess { if (it.isEmpty()) throw Exception() }

    override fun saveHeroes(list: List<HeroEntity>) = mHeroesDao.insertAll(list.toMutableList())

    override fun getHeroByLocalizedName(hero: String): Single<List<HeroEntity>> =
            mHeroesDao.getHeroByLocalName(hero).firstOrError()
                    .doOnSuccess { if (it.isEmpty()) throw Exception() }
}