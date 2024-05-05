package com.malqaa.androidappp.newPhase.data.network.retrofit

import com.google.gson.GsonBuilder
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.data.network.service.MalqaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {
    private val httpClient = OkHttpClient.Builder()
    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    private var builder: Retrofit.Builder? = null
    init {
        if (httpLoggingInterceptor != null) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val authenticationInterceptor = AuthenticationInterceptor()
            httpClient.addInterceptor(httpLoggingInterceptor)
            httpClient.addInterceptor(authenticationInterceptor)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
            httpClient.writeTimeout(2, TimeUnit.MINUTES)
            httpClient.connectTimeout(2, TimeUnit.MINUTES)

            if (builder == null) {
                builder = Retrofit.Builder()
                    .baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory( GsonConverterFactory.create(
//                        GsonBuilder()
//                            .setLenient()
//                            .create()
                    ))
                println("hhhh " + Constants.API_BASE_URL)
                builder!!.client(httpClient.build())
            }
        }
    }

    fun getRetrofitBuilder(): MalqaApiService {
        return builder!!.build().create(MalqaApiService::class.java)
    }

}