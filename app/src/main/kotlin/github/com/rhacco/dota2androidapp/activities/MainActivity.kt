/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package github.com.rhacco.dota2androidapp.activities

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.utilities.SharedPreferencesHelper
import github.com.rhacco.dota2androidapp.utilities.appIsMissingPermissions
import github.com.rhacco.dota2androidapp.utilities.deviceIsOnline
import github.com.rhacco.dota2androidapp.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseLifecycleActivity<ReposViewModel>() {
    override val mViewModelClass = ReposViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (resources.getString(R.string.api_key).isEmpty()) {
            Toast.makeText(applicationContext,
                    resources.getString(R.string.error_no_api_key),
                    Toast.LENGTH_LONG).show()
            return
        }

        if (appIsMissingPermissions(applicationContext))
            return

        setContentView(R.layout.activity_main)
        initialDisplay()
        observeLiveData()

        showLiveMatchesButton.setOnClickListener {
            startActivity(Intent(this, LiveMatchesActivity::class.java))
        }
        setFavHeroButton.setOnClickListener { setNewFavoriteHero() }
    }

    private fun initialDisplay() {
        val favoriteHero = SharedPreferencesHelper(applicationContext).getFavoriteHero()
        if (favoriteHero.isNotEmpty()) {
            Toast.makeText(this,
                    resources.getString(R.string.loaded_fav_hero, favoriteHero),
                    Toast.LENGTH_LONG).show()
            favHeroText.text = resources.getString(R.string.loaded_fav_hero_display, favoriteHero)
        } else
            favHeroText.text = resources.getString(R.string.init_fav_hero_display)
    }

    private fun setNewFavoriteHero() {
        val alert = AlertDialog.Builder(this)
        alert.setMessage(resources.getString(R.string.ask_fav_hero))
        val inputField = EditText(this)
        alert.setView(inputField)
        alert.setPositiveButton(resources.getString(R.string.dialog_ok)) { _, _ ->
            mViewModel.getHero(inputField.text.toString())
        }
        alert.setNegativeButton(resources.getString(R.string.dialog_cancel)) { _, _ -> }
        alert.show()
    }

    // Check if userInput is a valid (currently available) Dota 2 hero.
    // If it is, save it to device storage. If it isn't, return to the input dialog.
    private fun validateUserInput(userInput: String, listOfHeroes: List<HeroEntity>) =
            if (listOfHeroes.isNotEmpty()) {
                SharedPreferencesHelper(applicationContext).setFavoriteHero(userInput)
                Toast.makeText(this, resources.getString(R.string.saved_fav_hero, userInput),
                        Toast.LENGTH_LONG).show()
                favHeroText.text = resources.getString(R.string.loaded_fav_hero_display, userInput)
            } else {
                Toast.makeText(this, resources.getString(R.string.error_invalid_hero, userInput),
                        Toast.LENGTH_LONG).show()
                setNewFavoriteHero()
            }

    // Observe actions on data fetched from the official Dota 2 API and react accordingly.
    private fun observeLiveData() {
        if (!deviceIsOnline(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)) {
            Toast.makeText(applicationContext,
                    resources.getString(R.string.error_querying_dota2api_failed) + " no internet connection",
                    Toast.LENGTH_LONG).show()
            return
        }

        mViewModel.mHeroListSearchQueryLiveData.observe(this, Observer<Pair<String, List<HeroEntity>>> {
            it?.let { (first, second) -> validateUserInput(first, second) }
        })
    }
}
