package com.malka.androidappp

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this);
        Fresco.initialize(getApplicationContext());
        Lingver.init(this, "en")
    }
}