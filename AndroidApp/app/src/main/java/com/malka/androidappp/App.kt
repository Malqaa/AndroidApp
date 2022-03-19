package com.malka.androidappp

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import io.paperdb.Paper

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this);
        Fresco.initialize(getApplicationContext());

    }
}