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
import android.os.Bundle
import android.widget.EditText
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseNavigationDrawerActivity
import github.com.rhacco.dota2androidapp.utilities.SharedPreferencesHelper
import github.com.rhacco.dota2androidapp.utilities.appIsMissingPermissions
import kotlinx.android.synthetic.main.activity_main.*
import xdroid.toaster.Toaster

class MainActivity : BaseNavigationDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.initNavigationDrawer(drawer_layout)

        if (appIsMissingPermissions(applicationContext))
            return

        initialDisplay()

        setFavHeroButton.setOnClickListener { setNewFavoriteHero() }
    }

    private fun initialDisplay() {
        val favoriteHero = SharedPreferencesHelper(applicationContext).getFavoriteHero()
        if (favoriteHero.isNotEmpty()) {
            Toaster.toastLong(R.string.loaded_fav_hero, favoriteHero)
            favHeroText.text = getString(R.string.loaded_fav_hero_display, favoriteHero)
        } else
            favHeroText.text = getString(R.string.init_fav_hero_display)
    }

    private fun setNewFavoriteHero() {
        val alert = AlertDialog.Builder(this)
        alert.setMessage(getString(R.string.ask_fav_hero))
        val inputField = EditText(this)
        alert.setView(inputField)
        alert.setPositiveButton(getString(R.string.dialog_ok)) { _, _ -> saveFavoriteHero(inputField.text.toString()) }
        alert.setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
        alert.show()
    }

    private fun saveFavoriteHero(userInput: String) {
        SharedPreferencesHelper(applicationContext).setFavoriteHero(userInput)
        Toaster.toastLong(R.string.saved_fav_hero, userInput)
        favHeroText.text = getString(R.string.loaded_fav_hero_display, userInput)
    }
}
