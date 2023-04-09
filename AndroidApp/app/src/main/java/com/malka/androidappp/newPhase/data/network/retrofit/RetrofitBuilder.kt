package com.malka.androidappp.newPhase.data.network.retrofit

import com.google.gson.GsonBuilder
import com.malka.androidappp.BuildConfig
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class RetrofitBuilder {

    private var okHttpClient: OkHttpClient.Builder? = null
    private var retrofit: Retrofit? = null

    companion object {
        private val httpClient = OkHttpClient.Builder()

        var builder: Retrofit.Builder? = null
        fun GetRetrofitBuilder(): MalqaApiService {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val authenticationInterceptor = AuthenticationInterceptor();
            httpClient.addInterceptor(httpLoggingInterceptor);
            httpClient.addInterceptor(authenticationInterceptor);
            httpClient.readTimeout(1, TimeUnit.MINUTES)
            httpClient.writeTimeout(2, TimeUnit.MINUTES)
            httpClient.connectTimeout(2, TimeUnit.MINUTES)
            if (builder == null) {
                builder = Retrofit.Builder()
                builder!!.baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                println("hhhh " + Constants.API_BASE_URL)
                builder!!.client(httpClient.build());
            }
            return builder!!.build().create(MalqaApiService::class.java)
        }

    }

    fun getClient(): MalqaApiService? {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        okHttpClient = getUnsafeOkHttpClient()
        okHttpClient?.addInterceptor { chain ->
            var request = chain.request()
            if (HelpFunctions.isUserLoggedIn()) {

                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + ConstantObjects.logged_authtoken)
                    .addHeader("User-Language", ConstantObjects.currentLanguage)
                    .addHeader("Provider-Id", ConstantObjects.logged_userid)
                    .addHeader("Accept", "*/*")
                    .addHeader("Content-Type", "multipart/form-data")
//                    .addHeader("Accept-Encoding", "application/json")
                   // .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .build()
            } else {
                request = request.newBuilder()
                    .addHeader("User-Language", ConstantObjects.currentLanguage)
//                   .addHeader("Content-Type", "application/json")
                   // .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .build()
            }
//            val response = chain.proceed(request)
//            response.code()
            chain.proceed(request)
        }?.addInterceptor(logging)

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setLenient()
            .create()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient?.build())
                .build()
        }
        return retrofit?.create(MalqaApiService::class.java)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(
                sslSocketFactory,
                (trustAllCerts[0] as X509TrustManager)
            )
            builder.readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    response.code //status code
                    response
                })
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}