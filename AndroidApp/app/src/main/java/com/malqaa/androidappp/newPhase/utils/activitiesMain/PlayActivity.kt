package com.malqaa.androidappp.newPhase.utils.activitiesMain

//import com.google.android.exoplayer2.source.MediaSource
//import com.google.android.exoplayer2.source.ProgressiveMediaSource
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
//import com.google.android.exoplayer2.util.Util

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import kotlinx.android.synthetic.main.activity_play.fbButtonBack
import kotlinx.android.synthetic.main.activity_play.playerView
import kotlinx.android.synthetic.main.activity_play.videoView


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


        val player = ExoPlayer.Builder(this).build()
        playerView.setPlayer(player)
        // Create a data source factory
        val dataSourceFactory = DefaultDataSource.Factory(
            this,
            DefaultHttpDataSource.Factory()
                .setUserAgent(Util.getUserAgent(this, getString(R.string.app_name)))
        )

        // Create a media source
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))
        // Prepare the player with the media source
        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()
    }

    override fun onDestroy() {
        super.onDestroy()
//        andExoPlayerView.pausePlayer()

    }
}