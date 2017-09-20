package github.com.raccok.dota2androidapp.sources.repos

import github.com.raccok.dota2androidapp.entities.HeroEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

object HerosRepository : HerosDataSource {

    override fun getRepositories(): Single<List<HeroEntity>>
            = HerosLocalDataSource
            .getRepositories()
            .onErrorResumeNext {
                HerosRemoteDataSource.getRepositories()
                        .doOnSuccess { HerosLocalDataSource.saveRepositories(it) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun getHeroByLocalName(localName: String): Single<List<HeroEntity>>
            = HerosLocalDataSource
            .getHeroByLocalName(localName)
            .onErrorResumeNext {
                HerosRemoteDataSource.getRepositories()
                        .doOnSuccess {
                            HerosLocalDataSource.saveRepositories(it) }
                        .map(Function<List<HeroEntity>, List<HeroEntity>> { heroEntities ->
                            for (entity in heroEntities) {
                                if (entity.localized_name == localName) {
                                    return@Function listOf(entity)
                                }
                            }
                            return@Function listOf()
                        })
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}