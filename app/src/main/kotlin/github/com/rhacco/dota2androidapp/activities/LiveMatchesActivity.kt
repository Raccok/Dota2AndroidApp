package github.com.rhacco.dota2androidapp.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import github.com.rhacco.dota2androidapp.R
import kotlinx.android.synthetic.main.activity_live_matches.*

class LiveMatchesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_matches)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        testText.text = "Show overviews of interesting live games here"
    }
}