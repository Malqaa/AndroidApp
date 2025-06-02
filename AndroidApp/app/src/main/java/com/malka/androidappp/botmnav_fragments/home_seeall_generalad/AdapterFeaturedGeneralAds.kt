package com.malka.androidappp.botmnav_fragments.home_seeall_generalad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.Generaladvetisement
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso

class AdapterFeaturedGeneralAds(
    val listCar: List<Generaladvetisement>?
) :
    RecyclerView.Adapter<AdapterFeaturedGeneralAds.CarViewHolder>() {

    var onItemClick: ((Generaladvetisement) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itemselling_cardview2, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val list: Generaladvetisement = listCar!![position]
        holder.bind(list)
    }


    override fun getItemCount(): Int {
        return if(listCar!=null) listCar!!.size else 0
    }

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ttname: TextView? = null
        var minutess: TextView? = null
        var description: TextView? = null
        var pricee: TextView? = null
        var myimg: ImageView? = null

        init {
            ttname = itemView.findViewById(R.id.titlenamee_featuredgeneral)
            minutess = itemView.findViewById(R.id.sellxldate)
            description = itemView.findViewById(R.id.descrip_featuredgeneral)
            pricee = itemView.findViewById(R.id.pricee_featuredgeneral)
            myimg = itemView.findViewById(R.id.myimagee_featuredgeneral)
            itemView.setOnClickListener {
                onItemClick?.invoke(listCar!!.get(adapterPosition))
            }
        }

        fun bind(listCar: Generaladvetisement) {
            //It is ttname.text = listCar.name and similarly others
            ttname?.let {
                it.show()
                it.text = if (listCar.name != null) listCar.name else ""
            } ?: run {
                ttname!!.hide()
            }
            minutess?.let {
                it.show()
                it.text = if (listCar.city != null) listCar.city else ""
            } ?: run {
                minutess!!.hide()

            }
            description?.let {
                it.show()
                it.text = if (listCar.country != null) listCar.country else ""
            } ?: run {
                description!!.hide()
            }
            pricee?.let {
                it.text = if (listCar.price != null) listCar.price else ""
            } ?: run {
                pricee!!.hide()
            }
            myimg?.let {
                it.show()
                //When Url is available in payload  use this line
                //  Picasso.get().load(listCar.image).into(it);
                if (listCar.homepageImage != null) Picasso.get()
                    .load(ApiConstants.IMAGE_URL + listCar.homepageImage)
                    .into(it);
                else  it.setImageResource(R.drawable.car)

            } ?: run {
                myimg!!.hide()

            }
        }

    }
}