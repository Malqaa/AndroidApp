package com.malka.androidappp.botmnav_fragments.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.botmnav_fragments.home.model.Generaladvetisement
import com.malka.androidappp.botmnav_fragments.home.model.Propertyadvetisement
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso

class GeneralAdvertisementAdapter(
    val listCar: List<Generaladvetisement>?
) :
    RecyclerView.Adapter<GeneralAdvertisementAdapter.CarViewHolder>() {

    var onItemClick: ((Generaladvetisement) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
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
        var adCountry:TextView?=null
        var auction:TextView?=null

        init {
            ttname = itemView.findViewById(R.id.titlenamee)
            minutess = itemView.findViewById(R.id.minutes)
            description = itemView.findViewById(R.id.descrip)
            pricee = itemView.findViewById(R.id.pricee)
            adCountry = itemView.findViewById(R.id.country)
            auction = itemView.findViewById(R.id.auction)
            myimg = itemView.findViewById(R.id.myimagee)
            itemView.setOnClickListener {
                onItemClick?.invoke(listCar!!.get(adapterPosition))
            }
        }

        fun bind(listCar: Generaladvetisement) {
            //It is ttname.text = listCar.name and similarly others
            ttname?.let {
                it.show()
                it.text = if (listCar.title != null) listCar.title else ""
            } ?: run {
                ttname!!.hide()
            }
            minutess?.let {
                it.show()
                it.text = if (listCar.subtitle != null) listCar.subtitle else ""
            } ?: run {
                minutess!!.hide()

            }
            description?.let {
                it.show()
                it.text = if (listCar.city != null) listCar.city else ""
            } ?: run {
                description!!.hide()
            }
            adCountry?.let {
                it.show()
                it.text = if (listCar.country != null) listCar.country else ""
            } ?: run {
                adCountry!!.hide()
            }
            auction?.let {
                it.show()
                it.text = if (listCar.startingPrice != null) listCar.startingPrice else ""
            } ?: run {
                auction!!.hide()
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
                else it.setImageResource(R.drawable.car)
            } ?: run {
                myimg!!.hide()

            }
        }

    }
}