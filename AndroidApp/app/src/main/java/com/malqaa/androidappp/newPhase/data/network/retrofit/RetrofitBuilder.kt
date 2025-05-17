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
            httpClient.addInterceptor(CurlLoggingInterceptor())
            httpClient.hostnameVerifier(hostnameVerifier)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
            httpClient.writeTimeout(2, TimeUnit.MINUTES)
            httpClient.connectTimeout(2, TimeUnit.MINUTES)


//            var builder = OkHttpClient.Builder()
//            try {
//                // Load your custom CA
//                val cf = CertificateFactory.getInstance("X.509")
//                val caInput: InputStream =
//                    BufferedInputStream(FileInputStream("path/to/your/ca.crt"))
//                val ca: Certificate
//                ca = try {
//                    cf.generateCertificate(caInput)
//                } finally {
//                    caInput.close()
//                }
//
//                // Create a KeyStore containing your trusted CAs
//                val keyStoreType = KeyStore.getDefaultType()
//                val keyStore = KeyStore.getInstance(keyStoreType)
//                keyStore.load(null, null)
//                keyStore.setCertificateEntry("ca", ca)
//
//                // Create a TrustManager that trusts the CAs in your KeyStore
//                val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
//                val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
//                tmf.init(keyStore)
//
//                // Create an SSLContext that uses your TrustManager
//                val sslContext = SSLContext.getInstance("TLS")
//                sslContext.init(null, tmf.trustManagers, SecureRandom())
//                httpClient.sslSocketFactory(
//                    sslContext.socketFactory,
//                    tmf.trustManagers[0] as X509TrustManager
//                )
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
            if (builder == null) {
                builder = Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(Constants.API_BASE_URL)
                    .addConverterFactory(
                        GsonConverterFactory.create(
//                        GsonBuilder()
//                            .setLenient()
//                            .create()
                        )
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