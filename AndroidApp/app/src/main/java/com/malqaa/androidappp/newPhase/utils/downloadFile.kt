package com.malqaa.androidappp.newPhase.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.malqaa.androidappp.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun downloadFile(context: Context, url: String) {
    val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    executorService.execute {
        try {
            val finalUrl = followRedirects(context, url)
            val fileName = finalUrl.getFileNameFromUrl()

            val request = DownloadManager.Request(Uri.parse(finalUrl))
                .setTitle(context.getString(R.string.downloading, fileName))
                .setDescription(context.getString(R.string.please_wait))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            (context as Activity).runOnUiThread {
                Toast.makeText(
                    context,
                    context.getString(R.string.download_started),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            (context as Activity).runOnUiThread {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_unexpected, e.localizedMessage),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

// Function to follow redirects and return final URL
fun followRedirects(context: Context, url: String): String {
    var currentUrl = url
    var redirectCount = 0

    while (true) {
        val urlConnection = URL(currentUrl).openConnection() as HttpURLConnection
        urlConnection.instanceFollowRedirects = false
        urlConnection.requestMethod = "HEAD"
        val responseCode = urlConnection.responseCode

        Log.i("Redirect Check", "Response Code: $responseCode for URL: $currentUrl")

        if (responseCode in 300..399) {
            val redirectUrl = urlConnection.getHeaderField("Location")
            if (redirectUrl != null) {
                currentUrl = redirectUrl
                redirectCount++
                if (redirectCount > 5) {
                    throw IOException(context.getString(R.string.error_too_many_redirects))
                }
            } else {
                throw IOException(context.getString(R.string.error_redirect_without_location))
            }
        } else if (responseCode == HttpURLConnection.HTTP_OK) {
            return currentUrl
        } else {
            throw IOException(context.getString(R.string.error_failed_download, responseCode))
        }
    }
}

// Extract file name from URL
fun String.getFileNameFromUrl(): String {
    return this.substringAfterLast('/')
}
