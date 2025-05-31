package com.malqaa.androidappp.newPhase.data.network.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CurlLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val curlCommand = buildCurlCommand(request)
        Log.d("CURL", curlCommand)
        return chain.proceed(request)
    }

    private fun buildCurlCommand(request: Request): String {
        val curlBuilder = StringBuilder("curl -X ${request.method} \"${request.url}\"")

        // Add headers
        for (headerName in request.headers.names()) {
            val value = request.header(headerName)
            curlBuilder.append(" -H \"$headerName: $value\"")
        }

        // Add body if present
        request.body?.let { body ->
            try {
                val buffer = okio.Buffer()
                body.writeTo(buffer)
                val charset = body.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
                val bodyString = buffer.readString(charset)
                curlBuilder.append(" --data '${bodyString}'")
            } catch (e: Exception) {
                Log.e("CURL", "Could not read request body", e)
            }
        }

        return curlBuilder.toString()
    }
}
