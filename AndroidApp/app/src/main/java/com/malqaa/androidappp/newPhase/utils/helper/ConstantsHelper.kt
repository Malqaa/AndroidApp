package com.malqaa.androidappp.newPhase.utils.helper

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import com.google.gson.Gson
import com.malqaa.androidappp.newPhase.domain.models.CountryCode
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat


object ConstantsHelper {


    const val Channel_Name = "com.malqaa.androidApp.fcm"
    const val CHANNEL_ID = "OnRuf_channel"
    var fcmToken=""
    enum class PermissionEnum {
        PICK_CAMERA,
        READ_GALLERY_STORAGE,
    }

    fun convertTimeToLong(timeString: String): Long {
        return try {
            val sdf = SimpleDateFormat("HH:mm:ss")
            val date = sdf.parse(timeString)
            date.time
        } catch (e: ParseException) {
            e.printStackTrace()
            -1 // Handle the exception according to your requirements
        }
    }

    fun readJson(con: Context): Array<CountryCode>? {
        val countryCodes: Array<CountryCode>
//        val countryCode: ArrayList<CountryCode>
        try {
            val cc: String = assetJSONFile("CountryCodes.json", con)
            val gson = Gson()
            countryCodes = gson.fromJson(cc, Array<CountryCode>::class.java)
//            countryCode = ArrayList(listOf(*countryCodes))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return countryCodes
    }

    fun assetJSONFile(filename: String, context: Context): String {
        val manager = context.assets
        val file = manager.open(filename)
        val formArray = ByteArray(file.available())
        file.read(formArray)
        file.close()
        return String(formArray)
    }

    fun Context.openRawResourceByName(vImageView: ImageView, resourceName: String?) {
        try {
            val resources: Resources = resources
            val resourceId = resources.getIdentifier(resourceName, "raw", packageName)
            if (resourceId != 0) {
                val svg1 = SVG.getFromInputStream(resources.openRawResource(resourceId))
                vImageView.setImageDrawable(PictureDrawable(svg1.renderToPicture()))
            }
        } catch (e: ExceptionInInitializerError) {
            e.printStackTrace()
        }
    }

    fun getMultiPart(file: File?, typeRequestBody: String, nameFile: String): MultipartBody.Part? {
        var multipartBody: MultipartBody.Part? = null
        file?.let {
            val requestBody: RequestBody = it.asRequestBody(typeRequestBody.toMediaTypeOrNull())
            multipartBody = MultipartBody.Part.createFormData(nameFile, it.name, requestBody)
        }
        return multipartBody
    }
}
