package com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.shop_cloth_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.CategoriesItem

class ShopClothAdap(
    val categories: Categories,
    var clickListener: onShopClothClickListener
) : RecyclerView.Adapter<ShopClothAdap.ShopClothAdapViewHolder>()
{

    class ShopClothAdapViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val categoryName : TextView = itemview.findViewById(R.id.categoryName)
        fun initialize(item: CategoriesItem, action: onShopClothClickListener){
            categoryName.text = item.name
            itemView.setOnClickListener(){action.onItemClick(item, adapterPosition)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopClothAdapViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rows_vehicle_category,parent,false)
        return ShopClothAdapViewHolder(view)
    }


    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ShopClothAdapViewHolder, position: Int) {

        holder.initialize(categories.get(position),clickListener)
    }

    interface onShopClothClickListener{
        fun onItemClick(item: CategoriesItem, position: Int)
    }



}