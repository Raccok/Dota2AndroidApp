package github.com.raccok.dota2androidapp

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainFrontend {
  private lateinit var mMainBackend: MainBackend
  private lateinit var mAppContext: Context
  private lateinit var mMainTextView: TextView
  private lateinit var mMainAlert: AlertDialog.Builder

  fun init(mainBackend: MainBackend, appContext: Context, mainTextView: TextView,
           mainAlert: AlertDialog.Builder) {
    mMainBackend = mainBackend
    mAppContext = appContext
    mMainTextView = mainTextView
    mMainAlert = mainAlert

    initialDisplay()
  }

  fun initialDisplay() {
    mMainTextView.text = "Your favorite Dota 2 hero has not been defined yet."

    // Load the user's favorite Dota 2 hero from device storage or ask for it if not yet defined
    val favoriteHero = mMainBackend.loadFavoriteHero()
    if (favoriteHero != null && favoriteHero.isNotEmpty()) {
      Toast.makeText(mAppContext,
                     "Loaded your favorite Dota 2 hero '$favoriteHero' from device storage",
                     Toast.LENGTH_LONG).show()
      setFavoriteHeroText(favoriteHero)
    } else {
      mMainAlert.setTitle("Hello!")
      mMainAlert.setMessage("What is your favorite Dota 2 hero?")
      val inputField = EditText(mAppContext)
      mMainAlert.setView(inputField)
      mMainAlert.setPositiveButton("OK") { _, _ ->
        mMainBackend.saveFavoriteHero(inputField.text.toString())
      }
      mMainAlert.setNegativeButton("Cancel") { _, _ -> }
      mMainAlert.show()
    }
  }

  fun setFavoriteHeroText(name: String) {
    mMainTextView.text = "Your favorite Dota 2 hero is:\n\n$name\n\nWhat a fine choice!"
  }
}