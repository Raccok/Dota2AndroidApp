package github.com.raccok.dota2androidapp.sources.repos

import github.com.raccok.dota2androidapp.entities.HeroEntity
import github.com.raccok.dota2androidapp.sources.db.DatabaseCreator
import io.reactivex.Single

object HerosLocalDataSource : HerosDataSource {

    val reposDao = DatabaseCreator.database.reposDao()

    override fun getRepositories(): Single<List<HeroEntity>>
            = reposDao
            .loadAllHeros()
            .firstOrError()
            .doOnSuccess { if (it.isEmpty()) throw Exception() }

    override fun saveRepositories(list: List<HeroEntity>)
            = reposDao.insertAll(list.toMutableList())

    override fun getHeroByLocalName(localName: String): Single<List<HeroEntity>> = reposDao.getHeroByLocalName(localName).firstOrError()
            .doOnSuccess { if (it.isEmpty()) throw Exception() }


}