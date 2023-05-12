package com.malka.androidappp.newPhase.data.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_categories_card_in_home.view.*
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

    fun truncateString(str: String): String {
        var res = ""
        for (i in str.indices) {
            if (str[i].toString() == "-") {
                res = str.removeRange(i, str.length)
                break
            } else {
                res = str[i].toString()
            }
        }
        return res
    }

    fun clearPath() {

        AddProductObjectData.subcatone = ""
        AddProductObjectData.subcatonekey = ""

        AddProductObjectData.subcattwo = ""
        AddProductObjectData.subcattwokey = ""

        AddProductObjectData.subcatthree = ""
        AddProductObjectData.subcatthreekey = ""

        AddProductObjectData.subcatfour = ""
        AddProductObjectData.subcatfourkey = ""

        AddProductObjectData.subcatfive = ""
        AddProductObjectData.subcatfivekey = ""

        AddProductObjectData.subcatsix = ""
        AddProductObjectData.subcatsixkey = ""
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
        var imagePath= if(path==""||path==null) "emptyPath" else path
        Picasso.get()
            .load(imagePath)
            .error(R.drawable.main_logo)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    pb_loading?.hide()
                  //  onComplete?.invoke()
                }

                override fun onError(e: java.lang.Exception?) {
                    pb_loading?.hide()
                   // onComplete?.invoke()
                }

            })
    }

    fun Activity.shared(shareBody: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun Context.getDeviceId(): String {
        var id = Build.ID

        try {
            id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return id
    }

    fun String.requestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }
//    fun String.requestBody2(): RequestBody {
//        return RequestBody.create("text/plain".toMediaTypeOrNull(),this)
//    }
}