package github.com.rhacco.dotascoop.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.base.BaseNavigationDrawerActivity
import kotlinx.android.synthetic.main.web_view_drawer.*

class PatchesActivity : BaseNavigationDrawerActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view_drawer)
        super.initNavigationDrawer(drawer_layout)
        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = WebViewClient()
        web_view.loadUrl("http://liquipedia.net/dota2/Portal:Patches")
    }

    override fun onBackPressed() {
        if (web_view.canGoBack())
            web_view.goBack()
        else
            super.onBackPressed()
    }
}