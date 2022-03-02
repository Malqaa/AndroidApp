package com.malka.androidappp.botmnav_fragments.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.Const
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.design.product_details
import com.malka.androidappp.helper.BaseViewHolder
import com.malka.androidappp.helper.Extension.decimalNumberFormat
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.home.GeneralProduct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*

class GeneralAdvertisementAdapter(
    val listCar: List<GeneralProduct>,val currentfragment: Fragment
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var onItemClick: ((GeneralProduct) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.view.run {

            val params: ViewGroup.LayoutParams = fullview.layoutParams
            params.width = resources.getDimension(R.dimen._220sdp).toInt()
            params.height = params.height
            fullview.layoutParams = params

            listCar.get(position).run {


                titlenamee.text = title
                city_tv.text = city
                if(!price.isNullOrEmpty()){
                    LowestPrice.text = "${price.toDouble().decimalNumberFormat()} ${Const.currency}"
                }else{
                    LowestPrice.text = Const.currency

                }

                if (!price.isNullOrEmpty()){

                    LowestPrice_2.text = "${price.toDouble().decimalNumberFormat()} ${Const.currency}"

                }else{
                    LowestPrice.text = Const.currency
                }
                setOnClickListener {
                   // onItemClick?.invoke(listCar!!.get(position))

                    HelpFunctions.ViewAdvertismentDetail(
                        referenceId,
                        template,
                        currentfragment
                    )
                    SharedPreferencesStaticClass.ad_userid = user

                   // context.startActivity(Intent(context, product_details::class.java))
                }
                val imageURL = ApiConstants.IMAGE_URL + homepageImage
                Picasso.get()
                    .load(imageURL).error(R.drawable.car1)
                    .into(myimagee)

            }
        }


    }


    override fun getItemCount(): Int {
        return listCar.size
    }

}