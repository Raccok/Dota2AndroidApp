package github.com.raccok.dota2androidapp.Utilities

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface Dota2ApiService {
  @GET("IEconDOTA2_570/GetHeroes/v1")
  fun fetchLocalizedHeroData(@Query("key") steamApiKey: String,
                             @Query("language") language: String) : Observable<HeroDataModel.Result>

  companion object {
    private const val STEAM_API_URL = "http://api.steampowered.com/"

    fun create() : Dota2ApiService {
      val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                       .addConverterFactory(GsonConverterFactory.create())
                                       .baseUrl(STEAM_API_URL)
                                       .build()
      return retrofit.create(Dota2ApiService::class.java)
    }
  }
}
