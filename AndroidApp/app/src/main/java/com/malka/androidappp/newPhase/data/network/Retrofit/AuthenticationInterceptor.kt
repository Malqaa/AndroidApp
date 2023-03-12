package com.malka.androidappp.newPhase.data.network.Retrofit

import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val builder: Request.Builder = original.newBuilder()
            .header("Authorization", "Bearer ${ConstantObjects.logged_authtoken}")
        val request: Request = builder.build()
        return chain.proceed(request)
    }
}