package com.malka.androidappp.helper

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.ProductDetails
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.helper.Extension.decimalNumberFormat
import com.malka.androidappp.network.constants.ApiConstants
import kotlinx.android.synthetic.main.product_item.view.*

class GenericAdaptor {

    fun productAdaptor(
        element: AdDetailModel, context: Context, holder: BaseViewHolder, isGrid: Boolean
    ) {

        holder.view.run {
            element!!.run {

                titlenamee.text = title
                city_tv.text = city


                setOnClickListener {
                    SharedPreferencesStaticClass.ad_userid = user!!
                    context.startActivity(Intent(context, ProductDetails::class.java).apply {
                        putExtra("AdvId", referenceId)
                        putExtra("Template", template)
                    })
                }
                if (homepageImage.isNullOrEmpty()) {
                    if(!images.isNullOrEmpty()){
                        val imageURL = ApiConstants.IMAGE_URL + images.get(0)

                        Extension.loadThumbnail(
                            context,
                            imageURL,
                            productimg, loader
                        )
                    } else {
                        productimg.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.gray_light
                            )
                        )

                    }
                } else {
                    val imageURL = ApiConstants.IMAGE_URL + homepageImage
                    Extension.loadThumbnail(
                        context,
                        imageURL,
                        productimg, loader
                    )
                }


//                val  ItemInWatchlist = HelpFunctions.AdAlreadyAddedToWatchList(
//                    IndProperty.id
//                )


                date_tv.text = HelpFunctions.FormatDateTime(
                    createdOn!!,
                    HelpFunctions.datetimeformat_24hrs_7milliseconds_timezone,
                    HelpFunctions.datetimeformat_mmddyyyy
                )
                product_price.text = "${price!!.toDouble().decimalNumberFormat()} ${
                    context.getString(
                        R.string.Rayal
                    )
                }"
                LowestPrice.text = "${
                    price!!.toDouble().decimalNumberFormat()
                } ${context.getString(R.string.Rayal)}"


                LowestPrice.text =
                    "${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"
                LowestPrice_2.text =
                    "${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"
                if (isGrid) {
                    lisView.hide()
                    gridview.show()
                } else {
                    lisView.show()
                    gridview.hide()
                }
            }
        }
    }
}