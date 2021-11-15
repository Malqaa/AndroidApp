package com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.boatmarine_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.CategoriesItem
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.propertycategory.PropertyAdapter

class BoatsMarineAdapter(
    val categories: Categories,
    var clickListener: onBoatsmarineClickListener
) : RecyclerView.Adapter<BoatsMarineAdapter.BoatsMarineAdapterViewHolder>()
{

    class BoatsMarineAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val categoryName : TextView = itemview.findViewById(R.id.categoryName)
        fun initialize(item: CategoriesItem, action: onBoatsmarineClickListener){
            categoryName.text = item.name
            itemView.setOnClickListener(){action.onItemClick(item, adapterPosition)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoatsMarineAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rows_vehicle_category,parent,false)
        return BoatsMarineAdapterViewHolder(view)
    }


    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: BoatsMarineAdapterViewHolder, position: Int) {

        holder.initialize(categories.get(position),clickListener)
    }

    interface onBoatsmarineClickListener{
        fun onItemClick(item: CategoriesItem, position: Int)
    }



}