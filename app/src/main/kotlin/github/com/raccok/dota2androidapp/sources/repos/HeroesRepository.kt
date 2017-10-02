package github.com.raccok.dota2androidapp.sources.repos

import github.com.raccok.dota2androidapp.entities.HeroEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

object HeroesRepository : HeroesDataSource {
    override fun getRepositories(): Single<List<HeroEntity>> =
            HeroesLocalDataSource.getRepositories()
                    .onErrorResumeNext {
                        HeroesRemoteDataSource.getRepositories()
                                .doOnSuccess { HeroesLocalDataSource.saveRepositories(it) }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    override fun getHeroByLocalName(hero: String): Single<List<HeroEntity>> =
            HeroesLocalDataSource.getHeroByLocalName(hero)
                    .onErrorResumeNext {
                        HeroesRemoteDataSource.getRepositories()
                                .doOnSuccess {
                                    HeroesLocalDataSource.saveRepositories(it)
                                }
                                .map(Function<List<HeroEntity>, List<HeroEntity>> { heroEntities ->
                                    return@Function heroEntities
                                            .firstOrNull { it.localized_name == hero }
                                            ?.let { listOf(it) }
                                            ?: listOf()
                                })
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
