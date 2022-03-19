package com.malka.androidappp.helper

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream

// new since Glide v4
@GlideModule
class MyAppGlideModule : AppGlideModule() {
    // leave empty for now

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val factory: OkHttpUrlLoader.Factory = OkHttpUrlLoader.Factory(OkHttpClient())
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}