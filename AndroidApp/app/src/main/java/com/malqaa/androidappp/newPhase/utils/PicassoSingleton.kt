package com.malqaa.androidappp.newPhase.utils

import com.squareup.picasso.Picasso

object PicassoSingleton {
    private var picassoInstance: Picasso? = null

    fun getPicassoInstance(): Picasso {
        if (picassoInstance == null) {
            synchronized(PicassoSingleton::class.java) {
                if (picassoInstance == null) {
                    picassoInstance = Picasso.get()
                    // You can customize Picasso settings here if needed
                }
            }
        }
        return picassoInstance!!
    }
}