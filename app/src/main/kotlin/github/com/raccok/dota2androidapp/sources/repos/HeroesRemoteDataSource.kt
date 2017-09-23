package github.com.raccok.dota2androidapp.sources.repos

import github.com.raccok.dota2androidapp.App
import github.com.raccok.dota2androidapp.R
import github.com.raccok.dota2androidapp.entities.HeroEntity
import github.com.raccok.dota2androidapp.sources.remote.Dota2ApiService
import io.reactivex.Single

object HeroesRemoteDataSource : HeroesDataSource {
    override fun getHeroByLocalName(hero: String): Single<List<HeroEntity>> {
        TODO("not implemented") //DONT IMPLEMENT THIS HERE UNLESS THEY OPEN UP AN API CALL SPECIFICALLY ON THE DOTA2APISERVICE
    }

    private val mDota2ApiService by lazy { Dota2ApiService.create() }

    override fun getRepositories(): Single<List<HeroEntity>> =
            Single.create(
                    { subscriber ->
                        mDota2ApiService.fetchLocalizedHeroData(App.instance.getString(R.string.api_key), "en_us")
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