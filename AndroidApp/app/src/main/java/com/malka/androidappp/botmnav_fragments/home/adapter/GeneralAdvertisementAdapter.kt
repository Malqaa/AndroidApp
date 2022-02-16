package com.malka.androidappp.botmnav_fragments.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.Generaladvetisement
import com.malka.androidappp.helper.BaseViewHolder
import kotlinx.android.synthetic.main.product_item.view.*

class GeneralAdvertisementAdapter(
    val listCar: List<Generaladvetisement>
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var onItemClick: ((Generaladvetisement) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.view.run {

            val params: ViewGroup.LayoutParams = fullview.layoutParams
            params.width =    resources.getDimension(R.dimen._220sdp).toInt()
            params.height =  params.height
            fullview.layoutParams = params



//            listCar.get(position).run {
//                setOnClickListener {
//                    onItemClick?.invoke(listCar!!.get(position))
//                }
//            }
        }

//        //It is ttname.text = listCar.name and similarly others
//        ttname?.let {
//            it.show()
//            it.text = if (listCar.title != null) listCar.title else ""
//        } ?: run {
//            ttname!!.hide()
//        }
//        minutess?.let {
//            it.show()
//            it.text = if (listCar.subtitle != null) listCar.subtitle else ""
//        } ?: run {
//            minutess!!.hide()
//
//        }
//        description?.let {
//            it.show()
//            it.text = if (listCar.city != null) listCar.city else ""
//        } ?: run {
//            description!!.hide()
//        }
//        adCountry?.let {
//            it.show()
//            it.text = if (listCar.country != null) listCar.country else ""
//        } ?: run {
//            adCountry!!.hide()
//        }
//        auction?.let {
//            it.show()
//            it.text = if (listCar.startingPrice != null) listCar.startingPrice else ""
//        } ?: run {
//            auction!!.hide()
//        }
//        pricee?.let {
//            it.text = if (listCar.price != null) listCar.price else ""
//        } ?: run {
//            pricee!!.hide()
//        }
//        myimg?.let {
//            it.show()
//            //When Url is available in payload  use this line
//            //  Picasso.get().load(listCar.image).into(it);
//            if (listCar.homepageImage != null) Picasso.get()
//                .load(ApiConstants.IMAGE_URL + listCar.homepageImage)
//                .into(it);
//            else it.setImageResource(R.drawable.car)
//        } ?: run {
//            myimg!!.hide()
//
//        }

    }



    override fun getItemCount(): Int {
        return 4
    }

}