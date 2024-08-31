package com.malqaa.androidappp.newPhase.utils


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.malqaa.androidappp.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

fun downloadFile(context: Context, url: String) {
    val fileName = url.getFileNameFromUrl()

    try {
        // Check if the URL is valid and can be accessed
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        urlConnection.requestMethod = "HEAD"
        val responseCode = urlConnection.responseCode

        // If the response code is 200, proceed with the download
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(context.getString(R.string.downloading, fileName))
                .setDescription(context.getString(R.string.please_wait))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(
                context,
                context.getString(R.string.download_started),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Show error message if the URL cannot be accessed or doesn't return a valid file
            Toast.makeText(
                context,
                context.getString(R.string.error_cannot_download_from_the_provided_link),
                Toast.LENGTH_LONG
            ).show()
        }
    } catch (e: MalformedURLException) {
        // Handle invalid URL format
        Toast.makeText(
            context,
            context.getString(R.string.error_invalid_url),
            Toast.LENGTH_LONG
        ).show()
    } catch (e: UnknownHostException) {
        // Handle issues with DNS resolution or network connectivity
        Toast.makeText(
            context,
            context.getString(R.string.error_network_issue),
            Toast.LENGTH_LONG
        ).show()
    } catch (e: SocketTimeoutException) {
        // Handle connection timeout
        Toast.makeText(
            context,
            context.getString(R.string.error_connection_timeout),
            Toast.LENGTH_LONG
        ).show()
    } catch (e: IOException) {
        // Handle other I/O errors
        Toast.makeText(
            context,
            context.getString(R.string.error_io_exception, e.localizedMessage),
            Toast.LENGTH_LONG
        ).show()
    } catch (e: Exception) {
        // Handle any other unexpected exceptions
        e.printStackTrace()
        Toast.makeText(
            context,
            context.getString(R.string.error_unexpected, e.localizedMessage),
            Toast.LENGTH_LONG
        ).show()
    }
}

fun String.getFileNameFromUrl(): String {
    return this.substringAfterLast('/')
}

