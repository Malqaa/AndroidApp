package com.malqaa.androidappp.newPhase.utils


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.malqaa.androidappp.R
import java.net.HttpURLConnection
import java.net.URL

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

            Toast.makeText(context, context.getString(R.string.download_started), Toast.LENGTH_SHORT).show()
        } else {
            // Show error message if the URL cannot be accessed or doesn't return a valid file
            Toast.makeText(
                context,
                context.getString(R.string.error_cannot_download_from_the_provided_link),
                Toast.LENGTH_LONG
            ).show()
        }
    } catch (e: Exception) {
        // Handle any exceptions that occur during the connection
        e.printStackTrace()
        Toast.makeText(context,
            context.getString(R.string.error, e.localizedMessage), Toast.LENGTH_LONG).show()
    }
}

//
//fun downloadFile(context: Context, url: String) {
//    val fileName = url.getFileNameFromUrl()
//    val request = DownloadManager.Request(Uri.parse(url))
//        .setTitle("Downloading $fileName")
//        .setDescription("Please wait...")
//        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        .setAllowedOverMetered(true)
//        .setAllowedOverRoaming(true)
//        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//
//    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//    val downloadId = downloadManager.enqueue(request)
//
//    Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
//}

fun String.getFileNameFromUrl(): String {
    return this.substringAfterLast('/')
}

