package com.malqaa.androidappp.newPhase.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Extension {


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
//                    imageView.setImageDrawable(context.getDrawableCompat(R.mipmap.malqa_iconn_round))
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
        val imagePath= if(path==""||path==null) "emptyPath" else path


        Picasso.get()
            .load(imagePath)
            .error(R.mipmap.malqa_iconn)
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

    fun String.requestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}