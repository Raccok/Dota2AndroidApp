package github.com.raccok.dota2androidapp.sources.repos

import android.util.Log
import github.com.raccok.dota2androidapp.App
import github.com.raccok.dota2androidapp.R
import github.com.raccok.dota2androidapp.sources.remote.DotaHerosApiService
import github.com.raccok.dota2androidapp.entities.HeroEntity
import io.reactivex.Single

object HerosRemoteDataSource : HerosDataSource {
    override fun getHeroByLocalName(hero: String): Single<List<HeroEntity>> {
        TODO("not implemented") //DONT IMPLEMENT THIS HERE UNLESS THEY OPEN UP AN API CALL SPECIFICALLY ON THE DOTA2APISERVICE
    }

    private val dota2ApiService by lazy { DotaHerosApiService.create() }

    override fun getRepositories(): Single<List<HeroEntity>> =
            Single.create(
                    { subscriber ->
                        dota2ApiService.fetchLocalizedHeroData(App.instance.getString(R.string.api_key), "en_us")
                                .map { it?.component1() ?: throw Exception() }
                                .subscribe({ result ->
                                    subscriber.onSuccess(result.herosList())
                                },
                                        { _ ->
                                            subscriber.onError(Exception())
                                        })


                    }
            )
}