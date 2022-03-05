package com.malka.androidappp.helper

import android.content.Context
import com.malka.androidappp.Const
import com.malka.androidappp.R
import com.malka.androidappp.helper.Extension.decimalNumberFormat
import com.malka.androidappp.servicemodels.categorylistings.SearchRespone
import kotlinx.android.synthetic.main.product_item.view.*

class GenericAdaptor {

    fun productAdaptor(
        element: SearchRespone.Data.Source, context: Context, holder: BaseViewHolder,isGrid:Boolean
    ) {

        holder.view.run {
            element.run {
//                val  ItemInWatchlist = HelpFunctions.AdAlreadyAddedToWatchList(
//                    IndProperty.id
//                )

                titlenamee.text=title
              //  city_tv.text=city
                date_tv.text="26/6/2021"
                purchasing_price_tv.text="${price.toDouble().decimalNumberFormat()} ${context.getString(
                    R.string.Rayal)}"
                purchasing_price_tv_2.text="${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"


                LowestPrice.text="${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"
                LowestPrice_2.text="${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"
                setOnClickListener {

                }
                if(isGrid){
                    lisView.hide()
                    gridview.show()
                }else{
                    lisView.show()
                    gridview.hide()
                }
            }
        }
    }
}