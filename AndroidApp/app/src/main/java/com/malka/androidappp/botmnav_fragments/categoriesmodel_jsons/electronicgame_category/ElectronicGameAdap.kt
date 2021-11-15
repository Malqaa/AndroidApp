package com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.electronicgame_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.CategoriesItem


class ElectronicGameAdap (
    val categories: Categories,
    var clickListener: onElectronicGameClickListener
) : RecyclerView.Adapter<ElectronicGameAdap.ElectronicGameAdapViewHolder>()
{

    class ElectronicGameAdapViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val categoryName : TextView = itemview.findViewById(R.id.categoryName)
        fun initialize(item: CategoriesItem, action: onElectronicGameClickListener){
            categoryName.text = item.name
            itemView.setOnClickListener(){action.onItemClick(item, adapterPosition)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectronicGameAdapViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rows_vehicle_category,parent,false)
        return ElectronicGameAdapViewHolder(view)
    }


    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ElectronicGameAdapViewHolder, position: Int) {

        holder.initialize(categories.get(position),clickListener)
    }

    interface onElectronicGameClickListener{
        fun onItemClick(item: CategoriesItem, position: Int)
    }



}