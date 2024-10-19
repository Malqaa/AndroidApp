package com.malqaa.androidappp.newPhase.utils.activitiesMain

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
import com.malqaa.androidappp.databinding.ActivityPlayBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity

class PlayActivity : BaseActivity<ActivityPlayBinding>() {
    private var uri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fbButtonBack.setOnClickListener {
            finish()
        }
        uri = intent.getStringExtra("videourl")!!


        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.loadUrl(uri)


        val player = ExoPlayer.Builder(this).build()
        binding.playerView.setPlayer(player)
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
    }
}