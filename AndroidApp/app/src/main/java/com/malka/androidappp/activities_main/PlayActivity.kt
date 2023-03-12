package com.malka.androidappp.activities_main

import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : BaseActivity() {
    private var uri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        back_button.setOnClickListener {
            finish()
        }
        uri = intent.getStringExtra("videourl")!!
        andExoPlayerView.setSource(uri)
    }

    override fun onDestroy() {
        super.onDestroy()
        andExoPlayerView.pausePlayer()

    }
}