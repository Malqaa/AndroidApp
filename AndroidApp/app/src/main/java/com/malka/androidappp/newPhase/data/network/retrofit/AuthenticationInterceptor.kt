package com.malka.androidappp.newPhase.data.network.retrofit

import android.service.autofill.UserData
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import io.paperdb.Paper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var original: Request = chain.request()
        var builder: Request.Builder =
            if (HelpFunctions.isUserLoggedIn()) {
                println("hhhh "+ConstantObjects.logged_userid)
                println("hhhh "+ConstantObjects.logged_authtoken)
                original.newBuilder()
                    .addHeader("User-Language", ConstantObjects.currentLanguage)
                    .addHeader("Provider-Id", ConstantObjects.logged_userid)
                    .addHeader("Authorization", "Bearer ${ConstantObjects.logged_authtoken}")


            } else {
                original.newBuilder()
                    .addHeader("User-Language", ConstantObjects.currentLanguage)
            }


        var request: Request = builder.build()
        return chain.proceed(request)
    }
}