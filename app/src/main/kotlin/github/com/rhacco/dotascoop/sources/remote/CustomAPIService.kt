package github.com.rhacco.dotascoop.sources.remote

import github.com.rhacco.dotascoop.api.LeaderboardsResponse
import github.com.rhacco.dotascoop.api.TopMatchesResponse
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import github.com.rhacco.dotascoop.utilities.deviceIsOnline
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CustomAPIService {
    @GET("TopLiveMatches")
    fun fetchTopLiveMatches(): Observable<List<TopMatchesResponse.Match>>

    @GET("TopRecentMatches")
    fun fetchTopRecentMatches(): Observable<List<TopMatchesResponse.Match>>

    @GET("Heroes")
    fun fetchHeroes(): Observable<List<HeroEntity>>

    @GET("Items")
    fun fetchItems(): Observable<List<ItemEntity>>

    @GET("Leaderboard")
    fun fetchLeaderboard(@Query("region") region: String): Observable<List<LeaderboardsResponse.Entry>>

    companion object {
        private val sService by lazy { create() }
        private const val BASE_URL = "http://173.249.7.253:60742/"

        // Return null when the device is not connected to the internet so no query can be performed
        fun get(): CustomAPIService? =
                if (deviceIsOnline())
                    sService
                else
                    null

        private fun create(): CustomAPIService {
            val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(CustomAPIService::class.java)
        }
    }
}