package com.malka.androidappp.helper

import android.content.Context
import com.malka.androidappp.Const
import com.malka.androidappp.helper.Extension.decimalNumberFormat
import com.malka.androidappp.servicemodels.categorylistings.SearchRespone
import kotlinx.android.synthetic.main.product_item.view.*

class GenericAdaptor {

    fun productAdaptor(
        element: SearchRespone.Data.Source, context: Context, holder: BaseViewHolder,
    ) {

        holder.view.run {
            element.run {
                titlenamee.text=title
              //  city_tv.text=city
                date_tv.text="26/6/2021"
                purchasing_price_tv.text="${price.toDouble().decimalNumberFormat()} ${Const.currency}"
                LowestPrice.text="${price.toDouble().decimalNumberFormat()} ${Const.currency}"
                setOnClickListener {

                }
            }
        }
    }
}