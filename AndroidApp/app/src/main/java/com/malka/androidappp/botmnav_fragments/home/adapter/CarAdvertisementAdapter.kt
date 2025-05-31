package com.malka.androidappp.botmnav_fragments.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.CarAdvertisment
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.botmnav_fragments.home.model.Generaladvetisement
import com.malka.androidappp.botmnav_fragments.home.model.Propertyadvetisement
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso

class CarAdvertisementAdapter(
    val listCar: List<CarAdvertisment>?
) :
    RecyclerView.Adapter<CarAdvertisementAdapter.CarViewHolder>() {

    var onItemClick: ((CarAdvertisment) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val list: CarAdvertisment = listCar!![position]
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return if (listCar != null) listCar!!.size else 0
    }

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ttname: TextView? = null
        var minutess: TextView? = null
        var description: TextView? = null
        var pricee: TextView? = null
        var myimg: ImageView? = null

        init {
            ttname = itemView.findViewById(R.id.titlenamee)
            minutess = itemView.findViewById(R.id.minutes)
            description = itemView.findViewById(R.id.descrip)
            pricee = itemView.findViewById(R.id.pricee)
            myimg = itemView.findViewById(R.id.myimagee)
            itemView.setOnClickListener {
                onItemClick?.invoke(listCar!!.get(adapterPosition))
            }
        }

        fun bind(listCar: CarAdvertisment) {
            //It is ttname.text = listCar.name and similarly others
            ttname?.let {
                it.show()
                it.text = listCar.name
            } ?: run {
                ttname!!.hide()
            }
            minutess?.let {
                it.show()
                it.text = listCar.city
            } ?: run {
                minutess!!.hide()

            }
            description?.let {
                it.show()
                it.text = listCar.slug
            } ?: run {
                description!!.hide()
            }
            pricee?.let {
                it.text = listCar.price
            } ?: run {
                pricee!!.hide()
            }
            myimg?.let {
                it.show()
                //When Url is available in payload  use this line
                //  Picasso.get().load(listCar.image).into(it);
                Picasso.get()
                    .load(ApiConstants.IMAGE_URL + listCar.homepageImage)
                    .into(it);
            } ?: run {
                myimg!!.hide()

            }


        }

    }
}