package com.malqaa.androidappp.newPhase.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.malqaa.androidappp.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.regex.Pattern

object Extension {
    val desiredWidth = 800 // Specify desired width

    val desiredHeight = 600 // Specify desired height

    val requestOptions = RequestOptions()
        .override(desiredWidth, desiredHeight)
        .transform(RoundedCorners(12))
        .fitCenter() // or .centerCrop(), depending on how you want to fit the image

    fun Double.decimalNumberFormat(): String {

        val limit = 3

        var aNumber = ""
        try {
            val format = "%.${limit}f"
            val decimalNumber = String.format(Locale.ENGLISH, format, this)
            val pattern = "###,###,###,###,##0"

            val formatter = DecimalFormat(pattern, DecimalFormatSymbols(Locale.ENGLISH))
            aNumber = formatter.format(decimalNumber.toDouble())
        } catch (error: Exception) {
            error.printStackTrace()
            aNumber = this.toString()
        }
        return aNumber
    }

    fun loadImgGlide(
        context: Context,
        path: String?,
        imageView: ImageView,
        pb_loading: View? = null,
    ) {

        pb_loading?.show()
        Glide.with(context)
            .load(path ?: "")
            .apply(requestOptions)

            .error(R.mipmap.ic_launcher)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    pb_loading?.hide()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    pb_loading?.hide()
                    return false
                }

            })
            .into(imageView)
    }

    fun loadThumbnail(
        context: Context,
        path: String?,
        imageView: ImageView,
        pb_loading: View? = null,
        onComplete: (() -> Unit)? = null
    ) {
//        Glide.with(context)
//            .load(path)
//            .addListener(object : RequestListener<Drawable?> {
//                override fun onLoadFailed(
//                    @Nullable e: GlideException?,
//                    model: Any?,
//                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
//                    isFirstResource: Boolean,
//                ): Boolean {
//                    //  println("yyyy "+e?.message)
//                    if (pb_loading != null) {
//                        pb_loading.isVisible = false
//                        onComplete?.invoke()
//                    }
//                    imageView.setImageDrawable(context.getDrawableCompat(R.mipmap.ic_launcher_round))
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean,
//                ): Boolean {
//                    println("yyyy ready ")
//                    if (pb_loading != null) {
//                        pb_loading.isVisible = false
//                        onComplete?.invoke()
//
//                    }
//                    return false
//                }
//
//
//            })
//            .error(R.mipmap.malqa_iconn)
//            .into(imageView)
        pb_loading?.show()
        val imagePath = if (path == "" || path == null) "emptyPath" else path


        Picasso.get()
            .load(imagePath)
            .error(R.mipmap.ic_launcher)
//            .placeholder(R.mipmap.malqa_iconn)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    pb_loading?.hide()
                }

                override fun onError(e: Exception?) {
                    pb_loading?.hide()
                }
            })
    }

    fun Activity.shared(shareBody: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun Context.shared(shareBody: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun String.requestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}

// Extension function to check if a price is valid (non-null, not empty, and not zero)
fun String?.isValidPrice(): Boolean {
    return this?.toDoubleOrNull()?.let { it > 0.0 } ?: false
}

// Extension function to check if a price is valid (greater than zero)
fun Float.isValidPrice(): Boolean {
    return this > 0.0
}

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}

fun String.extractYouTubeVideoId(): String? {
    val regex = "v=([^&]+)".toRegex()
    val matchResult = regex.find(this)
    return matchResult?.groupValues?.get(1)
}

fun isYouTubeLink(url: String): Boolean {
    return url.contains("youtube.com") || url.contains("youtu.be")
}

fun extractYouTubeId(url: String): String? {
    // Logic to extract YouTube video ID from the URL
    val pattern = Pattern.compile("(?<=v=|/)([a-zA-Z0-9_-]{11})")
    val matcher = pattern.matcher(url)
    return if (matcher.find()) {
        matcher.group(1)
    } else null
}

fun isVideoLink(url: String): Boolean {
    val videoExtensions = listOf("mp4", "mkv", "webm", "avi", "mov")
    return videoExtensions.any { url.endsWith(it, ignoreCase = true) }
}

fun String.capitalizeFirstLetter(): String {
    return this.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun String?.formatAsCardNumber(): String {
    val cleanNumber = this?.filter { it.isDigit() }.orEmpty()

    return if (cleanNumber.length <= 4) {
        cleanNumber // Show it as is if too short
    } else {
        val maskedPart = "X".repeat(cleanNumber.length - 4)
            .chunked(4)
            .joinToString(" ")
        val visiblePart = cleanNumber.takeLast(4)
        "$maskedPart $visiblePart"
    }
}

fun String.getMonth() = this.split("/").getOrNull(index = 0)

fun String.getYear() = this.split("/").getOrNull(1)

