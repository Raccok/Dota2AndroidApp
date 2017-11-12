package github.com.rhacco.dota2androidapp.sources.remote

import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import github.com.rhacco.dota2androidapp.utilities.URLStrings
import github.com.rhacco.dota2androidapp.utilities.deviceIsOnline
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface OpenDotaAPIService {
    @GET("proPlayers")
    fun fetchProPlayers(): Observable<List<ProPlayerEntity>>

    companion object {
        private val sService by lazy { create() }

        // Return null when the device is not connected to the internet so no query can be performed
        fun get(): OpenDotaAPIService? =
                if (deviceIsOnline())
                    sService
                else
                    null

        private fun create(): OpenDotaAPIService {
            val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLStrings.OPENDOTA_API)
                    .build()
            return retrofit.create(OpenDotaAPIService::class.java)
        }
    }
}