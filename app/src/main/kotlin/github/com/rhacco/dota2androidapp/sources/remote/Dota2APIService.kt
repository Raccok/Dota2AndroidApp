package github.com.rhacco.dota2androidapp.sources.remote

import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import github.com.rhacco.dota2androidapp.utilities.deviceIsOnline
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Dota2APIService {
    @GET("ILeaderboard/GetDivisionLeaderboard/v0001")
    fun fetchLeaderboard(@Query("division") region: String): Observable<LeaderboardsResponse.Result>

    companion object {
        private val sService by lazy { create() }
        private const val BASE_URL = "http://www.dota2.com/webapi/"

        // Return null when the device is not connected to the internet so no query can be performed
        fun get(): Dota2APIService? =
                if (deviceIsOnline())
                    sService
                else
                    null

        private fun create(): Dota2APIService {
            val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(Dota2APIService::class.java)
        }
    }
}