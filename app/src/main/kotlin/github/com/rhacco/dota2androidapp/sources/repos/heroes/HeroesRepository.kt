package github.com.rhacco.dota2androidapp.sources.repos.heroes

import github.com.rhacco.dota2androidapp.entities.HeroEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

object HeroesRepository : HeroesDataSource {
    override fun getHeroes(): Single<List<HeroEntity>> =
            HeroesLocalDataSource.getHeroes()
                    .onErrorResumeNext {
                        HeroesRemoteDataSource.getHeroes()
                                .doOnSuccess { HeroesLocalDataSource.saveHeroes(it) }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getHeroesByIds(heroIds: List<Int>): Single<List<HeroEntity>> =
            HeroesLocalDataSource.getHeroesByIds(heroIds)
                    .onErrorResumeNext {
                        HeroesRemoteDataSource.getHeroes()
                                .doOnSuccess { HeroesLocalDataSource.saveHeroes(it) }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    override fun getHeroByLocalizedName(hero: String): Single<List<HeroEntity>> =
            HeroesLocalDataSource.getHeroByLocalizedName(hero)
                    .onErrorResumeNext {
                        HeroesRemoteDataSource.getHeroes()
                                .doOnSuccess { HeroesLocalDataSource.saveHeroes(it) }
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
