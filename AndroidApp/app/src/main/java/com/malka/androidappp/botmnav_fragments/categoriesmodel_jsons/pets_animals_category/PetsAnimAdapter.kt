package com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.pets_animals_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.CategoriesItem


class PetsAnimAdapter(
    val categories: Categories,
    var clickListener: onPetsAnimClickListener
) : RecyclerView.Adapter<PetsAnimAdapter.PetsAnimAdapterViewHolder>()
{

    class PetsAnimAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val categoryName : TextView = itemview.findViewById(R.id.categoryName)
        fun initialize(item: CategoriesItem, action: onPetsAnimClickListener){
            categoryName.text = item.name
            itemView.setOnClickListener(){action.onItemClick(item, adapterPosition)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsAnimAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rows_vehicle_category,parent,false)
        return PetsAnimAdapterViewHolder(view)
    }


    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: PetsAnimAdapterViewHolder, position: Int) {

        holder.initialize(categories.get(position),clickListener)
    }

    interface onPetsAnimClickListener{
        fun onItemClick(item: CategoriesItem, position: Int)
    }



}