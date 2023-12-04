package com.malka.androidappp.newPhase.data.network.retrofit

import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class AuthenticationInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val builder: Request.Builder =
            if (HelpFunctions.isUserLoggedIn()) {
                println("hhhh " + ConstantObjects.logged_userid)
                println("hhhh " + ConstantObjects.logged_authtoken)
                if (ConstantObjects.businessAccountUser == null)
                    original.newBuilder()
                        .addHeader("User-Language", ConstantObjects.currentLanguage)
                        .addHeader("Provider-Id", ConstantObjects.logged_userid)
                        .addHeader("Authorization", "Bearer ${ConstantObjects.logged_authtoken}")
                else
                    original.newBuilder()
                        .addHeader("User-Language", ConstantObjects.currentLanguage)
                        .addHeader("Provider-Id", ConstantObjects.logged_userid)
                        .addHeader(
                            "Business-Account-Id",
                            ConstantObjects.businessAccountUser!!.businessAccountId.toString()
                        )
                        .addHeader("Authorization", "Bearer ${ConstantObjects.logged_authtoken}")
            } else {
                original.newBuilder()
                    .addHeader("User-Language", ConstantObjects.currentLanguage)
            }


        val request: Request = builder.build()

        return chain.proceed(request)
    }
}