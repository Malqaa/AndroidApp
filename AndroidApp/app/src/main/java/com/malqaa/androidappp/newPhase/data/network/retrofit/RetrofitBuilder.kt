package com.malqaa.androidappp.newPhase.data.network.retrofit

import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.data.network.service.MalqaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession


object RetrofitBuilder {

    private val httpClient = OkHttpClient.Builder()
    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    private var builder: Retrofit.Builder? = null

    init {
        val hostnameVerifier = HostnameVerifier { hostname: String?, session: SSLSession? ->
            // Implement your custom hostname verification logic
            val defaultVerifier =
                HttpsURLConnection.getDefaultHostnameVerifier()
            defaultVerifier.verify(hostname, session)
        }
        if (httpLoggingInterceptor != null) {
            val authenticationInterceptor = AuthenticationInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(httpLoggingInterceptor)
            httpClient.addInterceptor(authenticationInterceptor)
            httpClient.hostnameVerifier(hostnameVerifier)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
            httpClient.writeTimeout(2, TimeUnit.MINUTES)
            httpClient.connectTimeout(2, TimeUnit.MINUTES)

            if (builder == null) {
                builder = Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory(
                        GsonConverterFactory.create()
                    )
                println("hhhh " + Constants.API_BASE_URL)
                builder!!.client(httpClient.build())
            }
        }
    }


    fun getRetrofitBuilder(): MalqaApiService {
        return builder!!.build().create(MalqaApiService::class.java)
    }

}