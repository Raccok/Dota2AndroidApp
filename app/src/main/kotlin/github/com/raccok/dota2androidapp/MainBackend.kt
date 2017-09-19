package github.com.raccok.dota2androidapp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import android.widget.Toast
import github.com.raccok.dota2androidapp.Utilities.Dota2ApiService
import github.com.raccok.dota2androidapp.Utilities.appIsMissingPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainBackend {
  private lateinit var mMainFrontend: MainFrontend
  private lateinit var mAppResources: Resources
  private lateinit var mAppContext: Context
  private lateinit var mAppPrefsGeneral: SharedPreferences
  private var mAppConnectivityMgr: ConnectivityManager? = null
  private var mHeroNames: List<String> = listOf()
  private val mDota2ApiService by lazy { Dota2ApiService.create() }
  private var mDisposable: Disposable? = null

  fun init(mainFrontend: MainFrontend, resources: Resources, context: Context,
           prefsGeneral: SharedPreferences, connectivityMgr: ConnectivityManager?) : Boolean {
    mMainFrontend = mainFrontend
    mAppResources = resources
    mAppContext = context
    mAppPrefsGeneral = prefsGeneral
    mAppConnectivityMgr = connectivityMgr

    if (mAppResources.getString(R.string.api_key).isEmpty()) {
      Toast.makeText(mAppContext,
                     "Need to provide a valid Steam Web API key in res/values/strings.xml!",
                     Toast.LENGTH_LONG).show()
      return false
    }

    if (appIsMissingPermissions(mAppContext))
      return false

    return true
  }

  fun loadFavoriteHero() : String? = mAppPrefsGeneral.getString(PREF_FAV_HERO, "")

  fun saveFavoriteHero(userInput: String) {
    if (mHeroNames.isEmpty())
      loadHeroNames(userInput)
    else
      handleUserInput(userInput)
  }

  private fun loadHeroNames(userInput: String) {
    if (!deviceIsOnline()) {
      Toast.makeText(mAppContext, HERO_QUERY_FAIL + "no internet connection",
                     Toast.LENGTH_LONG).show()
      return
    }

    // Try to fetch currently available Dota 2 heroes from the official Dota 2 API.
    // Store the progress in a Disposable so we can interrupt it when needed.
    mDisposable =
      mDota2ApiService.fetchLocalizedHeroData(mAppResources.getString(R.string.api_key), "en_us")
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe( { result -> mHeroNames = result.result.heroNamesLocalized()
                                              // Now handle the user input after loading is complete
                                              handleUserInput(userInput)},
                                  { error -> Toast.makeText(mAppContext,
                                                            HERO_QUERY_FAIL + error.message,
                                                            Toast.LENGTH_LONG).show() })
  }

  private fun deviceIsOnline(): Boolean {
    val netInfo = mAppConnectivityMgr?.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
  }

  // Check if userInput is a valid (currently available) Dota 2 hero.
  // If it is, save it to device storage. If it isn't, return to the input dialog.
  private fun handleUserInput(userInput: String) {
    if (mHeroNames.isEmpty()) {
      Toast.makeText(mAppContext, "Error: Dota 2 hero names not loaded", Toast.LENGTH_LONG).show()
      return
    }

    if (mHeroNames.contains(userInput)) {
      val e = mAppPrefsGeneral.edit()
      e?.putString(PREF_FAV_HERO, userInput)
      e?.commit()
      Toast.makeText(mAppContext,
                     "Saved your favorite hero '$userInput' to device storage",
                     Toast.LENGTH_LONG).show()
      mMainFrontend.setFavoriteHeroText(userInput)
    } else {
      Toast.makeText(mAppContext, "'$userInput' is not a valid Dota 2 hero!",
                     Toast.LENGTH_LONG).show()
      mMainFrontend.initialDisplay()
    }
  }

  fun activityDestroyed() {
    mDisposable?.dispose()
  }

  companion object {
    private const val PREF_FAV_HERO = "fav_hero"
    private const val HERO_QUERY_FAIL = "Querying heroes from the Dota 2 API failed: "
  }
}