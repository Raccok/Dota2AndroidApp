package github.com.raccok.dota2androidapp.sources.repos

import github.com.raccok.dota2androidapp.entities.HeroEntity
import github.com.raccok.dota2androidapp.sources.db.DatabaseCreator
import io.reactivex.Single

object HeroesLocalDataSource : HeroesDataSource {
    private val mReposDao = DatabaseCreator.mDatabase.reposDao()

    override fun getRepositories(): Single<List<HeroEntity>> =
            mReposDao.loadAllHeroes()
                    .firstOrError()
                    .doOnSuccess { if (it.isEmpty()) throw Exception() }

    override fun saveRepositories(list: List<HeroEntity>) = mReposDao.insertAll(list.toMutableList())

    override fun getHeroByLocalName(hero: String): Single<List<HeroEntity>> =
            mReposDao.getHeroByLocalName(hero).firstOrError()
                    .doOnSuccess { if (it.isEmpty()) throw Exception() }
}