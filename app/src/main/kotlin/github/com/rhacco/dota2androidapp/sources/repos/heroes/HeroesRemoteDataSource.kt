package github.com.rhacco.dota2androidapp.sources.repos.heroes

import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.sources.remote.Dota2OfficialAPIService
import io.reactivex.Single

object HeroesRemoteDataSource : HeroesDataSource {
    override fun getHeroes(): Single<List<HeroEntity>> =
            Single.create(
                    { subscriber ->
                        Dota2OfficialAPIService.get()
                                ?.fetchHeroesLocalized(App.instance.getString(R.string.api_key), "en_us")
                                ?.map { it.component1() }
                                ?.subscribe(
                                        { result -> subscriber.onSuccess(result.heroes) },
                                        { error -> subscriber.onError(error) })
                    }
            )

    // Don't implement this here unless they open up a specific API call on the official Dota 2 API
    override fun getHeroByLocalizedName(hero: String): Single<List<HeroEntity>> = TODO("not implemented")
}