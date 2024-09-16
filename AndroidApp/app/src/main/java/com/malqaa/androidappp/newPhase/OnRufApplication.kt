package com.malqaa.androidappp.newPhase

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class OnRufApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Paper.init(this);
        Fresco.initialize(applicationContext);
        Lingver.init(this, "en")
    }
}