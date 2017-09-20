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

package github.com.raccok.dota2androidapp

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.ik.exploringviewmodel.base.BaseLifecycleActivity
import com.ik.exploringviewmodel.flow.repos.ReposViewModel
import github.com.raccok.dota2androidapp.entities.HeroEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseLifecycleActivity<ReposViewModel>() {

    override val viewModelClass = ReposViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initContent()

        if (resources.getString(R.string.api_key).isEmpty()) {
            Toast.makeText(this,
                    resources.getString(R.string.error_no_api_key),
                    Toast.LENGTH_LONG).show()
            return
        }

        observeLiveData()

    }

    //TODO: move all operations pertaining to preferences to another class
    fun mAppPrefsGeneral(): SharedPreferences {
        return getSharedPreferences("general", Context.MODE_PRIVATE)
    }

    fun initContent() {
        textView.text = resources.getString(R.string.init_screen_hero_message)

        val favoriteHero = mAppPrefsGeneral().getString(PreferenceStrings.PREF_FAV_HERO, "")

        if (favoriteHero != null && favoriteHero.isNotEmpty()) {
            Toast.makeText(this,
                    resources.getString(R.string.loading_hero, favoriteHero),
                    Toast.LENGTH_LONG).show()

            textView.text = resources.getString(R.string.load_pref_hero_success_message, favoriteHero)
        } else {
            // Show a dialog to ask for the user's favorite hero
            var mMainAlert = AlertDialog.Builder(this)
            mMainAlert.setTitle(resources.getString(R.string.welcome_dialog_title)).setMessage(resources.getString(R.string.welcome_dialog_message))

            val input = EditText(this)
            mMainAlert.setView(input)

            mMainAlert.setPositiveButton(resources.getString(R.string.dialog_ok)) { _, _ ->

                viewModel.getHero(input.text.toString())
            }.setNegativeButton(resources.getString(R.string.dialog_cancel)) { _, _ -> }

            mMainAlert.show()
        }
    }

    private fun validateUserInput(userInput: String, listOfHeros: List<HeroEntity>) {
        if (!listOfHeros.isEmpty()) {
            //TODO: move all operations pertaining to preferences to another class
            // Put it into memory (don't forget to commit!)
            val e = mAppPrefsGeneral().edit()
            e?.putString(PreferenceStrings.PREF_FAV_HERO, userInput)
            e?.commit()

            Toast.makeText(this, resources.getString(R.string.success_pref_hero_commit, userInput),
                    Toast.LENGTH_LONG).show()
            textView.text = resources.getString(R.string.load_pref_hero_success_message, userInput)
        } else {
            Toast.makeText(this, resources.getString(R.string.error_invalid_hero, userInput),
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun observeLiveData() {
        viewModel.isLoadingLiveData.observe(this, Observer<Boolean> {
            it?.let {
                status ->
            }
        })
        viewModel.heroListSearchQueryLiveData.observe(this, Observer<Pair<String, List<HeroEntity>>> {
            it?.let {
                result ->
                validateUserInput(result.first, result.second)
            }
        })
        viewModel.throwableLiveData.observe(this, Observer<Throwable> {
            it?.let {
                result ->
            }
        })
    }

}
