package com.malka.androidappp.network.Retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import com.malka.androidappp.BuildConfig
import com.malka.androidappp.network.constants.Constants
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

class WebServiseClient(var context: Context) {
    // companion object Factory {
    private  var okHttpClient: OkHttpClient.Builder ?=null
    private var retrofit: Retrofit? = null
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getClient(): Retrofit? {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        okHttpClient = getUnsafeOkHttpClient()
        okHttpClient?.addInterceptor { chain ->
            var request = chain.request()
            if (request.header("Has-Authentication") != null) {
                request = request.newBuilder()
//                    .addHeader("Authorization", "Bearer " + globalPreferences.getUserData()?.token)
//                    .addHeader("Accept-Language", globalPreferences.getLang()!!)
                   // .addHeader("X-Api-Key", api_token)
                    .addHeader("Accept","application/json")
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Accept-Encoding", "application/json")
                     .build()
            } else {
                request = request.newBuilder()
                  //  .addHeader("Accept-Language", globalPreferences.getLang()!!)
                   // .addHeader("X-Api-Key", api_token)
                    .addHeader("Accept","application/json")
//                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
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
        return retrofit

    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
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
