package com.malqaa.androidappp.newPhase.utils.activitiesMain

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : BaseActivity() {
    private var uri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        fbButtonBack.setOnClickListener {
            finish()
        }
        uri = intent.getStringExtra("videourl")!!


        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()



        webView.loadUrl(uri)
//        andExoPlayerView.setSource(uri)
    }

    override fun onDestroy() {
        super.onDestroy()
        andExoPlayerView.pausePlayer()

    }
}