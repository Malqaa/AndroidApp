package com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.furniture_furnish_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.CategoriesItem

class FurnitureAdapter(
    val categories: Categories,
    var clickListener: onFurnitureClickListener
) : RecyclerView.Adapter<FurnitureAdapter.FurnitureAdapterViewHolder>()
{

    class FurnitureAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val categoryName : TextView = itemview.findViewById(R.id.categoryName)
        fun initialize(item: CategoriesItem, action: onFurnitureClickListener){
            categoryName.text = item.name
            itemView.setOnClickListener(){action.onItemClick(item, adapterPosition)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rows_vehicle_category,parent,false)
        return FurnitureAdapterViewHolder(view)
    }


    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: FurnitureAdapterViewHolder, position: Int) {

        holder.initialize(categories.get(position),clickListener)
    }

    interface onFurnitureClickListener{
        fun onItemClick(item: CategoriesItem, position: Int)
    }



}