package github.com.raccok.dota2androidapp.sources.repos

import github.com.raccok.dota2androidapp.App
import github.com.raccok.dota2androidapp.R
import github.com.raccok.dota2androidapp.entities.HeroEntity
import github.com.raccok.dota2androidapp.sources.remote.sDota2OfficialAPIService
import io.reactivex.Single

object HeroesRemoteDataSource : HeroesDataSource {
    // Don't implement this here unless they open up a specific API call on the official Dota 2 API
    override fun getHeroByLocalName(hero: String): Single<List<HeroEntity>> = TODO("not implemented")

    override fun getRepositories(): Single<List<HeroEntity>> =
            Single.create(
                    { subscriber ->
                        sDota2OfficialAPIService
                                .fetchLocalizedHeroData(App.instance.getString(R.string.api_key), "en_us")
                                .map { it.component1() }
                                .subscribe(
                                        { result ->
                                            subscriber.onSuccess(result.heroesList())
                                        },
                                        { _ ->
                                            subscriber.onError(Exception())
                                        })
                    }
            )
}