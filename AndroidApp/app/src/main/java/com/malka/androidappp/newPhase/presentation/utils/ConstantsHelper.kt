package com.malka.androidappp.newPhase.presentation.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.caverock.androidsvg.SVG
import com.google.gson.Gson
import com.malka.androidappp.newPhase.domain.models.CountryCode
import java.io.IOException


object ConstantsHelper {


    enum class PermissionEnum {
        PICK_CAMERA,
        READ_GALLERY_STORAGE,
    }

     fun readJson(con: Context):  Array<CountryCode>? {
        val countryCodes: Array<CountryCode>
        val countryCode: ArrayList<CountryCode>
        try {
            val cc: String = assetJSONFile("CountryCodes.json", con)
            val gson = Gson()
            countryCodes = gson.fromJson(cc, Array<CountryCode>::class.java)
            countryCode = ArrayList(listOf(*countryCodes))
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

    fun  Context.openRawResourceByName(vImageView: ImageView, resourceName: String?) {
        try {
            val resources: Resources = resources
            val resourceId = resources.getIdentifier(resourceName, "raw", packageName)
            if (resourceId != 0) {
                val svg1 = SVG.getFromInputStream( resources.openRawResource(resourceId))
                vImageView.setImageDrawable(PictureDrawable(svg1.renderToPicture()))
            }
        } catch (e: ExceptionInInitializerError) {
            e.printStackTrace()
        }
    }

}
