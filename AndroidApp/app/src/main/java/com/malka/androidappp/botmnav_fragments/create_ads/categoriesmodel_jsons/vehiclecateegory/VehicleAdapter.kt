package com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.vehiclecateegory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.CategoriesItem


class VehicleAdapter(
    val categories: Categories,
    var clickListener: onVehicleClickListener
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>()
{

    class VehicleViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val categoryName : TextView = itemview.findViewById(R.id.categoryName)
        fun initialize(item: CategoriesItem, action: onVehicleClickListener){
            categoryName.text = item.name
            itemView.setOnClickListener(){action.onItemClick(item, adapterPosition)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rows_vehicle_category,parent,false)
        return VehicleViewHolder(view)
    }


    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {

        holder.initialize(categories.get(position),clickListener)
    }

    interface onVehicleClickListener{
        fun onItemClick(item: CategoriesItem, position: Int)
    }



}