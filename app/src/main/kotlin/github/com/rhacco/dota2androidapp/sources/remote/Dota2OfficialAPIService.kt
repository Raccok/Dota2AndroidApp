package github.com.rhacco.dota2androidapp.sources.remote

import github.com.rhacco.dota2androidapp.api.HeroesResponse
import github.com.rhacco.dota2androidapp.api.RealtimeStatsResponse
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.utilities.URLStrings
import github.com.rhacco.dota2androidapp.utilities.deviceIsOnline
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val sDota2OfficialAPIService by lazy { Dota2OfficialAPIService.create() }

fun getDota2OfficialAPIService(): Dota2OfficialAPIService? =
        if (deviceIsOnline())
            sDota2OfficialAPIService
        else
            null

interface Dota2OfficialAPIService {
    @GET("IEconDOTA2_570/GetHeroes/v1")
    fun fetchHeroesLocalized(@Query("key") steamApiKey: String,
                             @Query("language") language: String): Observable<HeroesResponse.Result>

    @GET("IDOTA2Match_570/GetTopLiveGame/v1")
    fun fetchTopLiveGames(@Query("key") steamApiKey: String,
                          @Query("partner") partner: Int): Observable<TopLiveGamesResponse.Result>

    @GET("IDOTA2MatchStats_570/GetRealtimeStats/v1")
    fun fetchRealtimeStats(@Query("key") steamApiKey: String,
                           @Query("server_steam_id") serverSteamId: Long):
            Observable<RealtimeStatsResponse.Result>

    companion object {
        fun create(): Dota2OfficialAPIService {
            val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLStrings.STEAM_API)
                    .build()
            return retrofit.create(Dota2OfficialAPIService::class.java)
        }
    }
}
