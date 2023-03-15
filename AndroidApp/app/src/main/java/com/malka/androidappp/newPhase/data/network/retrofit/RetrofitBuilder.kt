package com.malka.androidappp.newPhase.data.network.retrofit

import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuilder {
    companion object {
        private val httpClient = OkHttpClient.Builder()
        var builder: Retrofit.Builder? = null
        fun GetRetrofitBuilder(): MalqaApiService {
            if (builder == null) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val authenticationInterceptor = AuthenticationInterceptor();
                httpClient.addInterceptor(httpLoggingInterceptor);

                httpClient.addInterceptor(authenticationInterceptor);
                builder = Retrofit.Builder()
                builder!!.baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                println("hhhh "+Constants.API_BASE_URL)
                builder!!.client(httpClient.build());
            }
            return builder!!.build().create(MalqaApiService::class.java)
        }
    }
}