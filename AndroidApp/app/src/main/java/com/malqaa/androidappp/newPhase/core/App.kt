package com.malqaa.androidappp.newPhase.core

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
//import com.microsoft.signalr.HubConnection
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper


class App: Application() {
//    lateinit var hubConnection: HubConnection

    override fun onCreate() {
        super.onCreate()
        Paper.init(this);
        Fresco.initialize(applicationContext);
        Lingver.init(this, "en")
        // Create a hub connection
//        hubConnection = HubConnectionBuilder.create("https://onrufwebsite6-001-site1.htempurl.com/chatHub")
//            .withTransport(com.microsoft.signalr.TransportEnum.LONG_POLLING) // or WebSockets if supported
//            .build()
//
//        // Start the connection
//        hubConnection.start().blockingAwait()
    }

}