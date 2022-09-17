package com.malka.androidappp.fragments.browse_market.popup_subcategories_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.fragments.create_ads.categoriesmodel_jsons.CategoriesItem
import kotlinx.android.synthetic.main.alert_subcategories_cardview.view.*


class AdapterSubCategories(
    val subcateposts: List<CategoriesItem>,
    var clicklistenersubcate:onPostItemClickLisenter
) : RecyclerView.Adapter<AdapterSubCategories.AdapterSubCategoriesViewHolder>()
{


    class AdapterSubCategoriesViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {

        val text1 : TextView = itemview.carr
        val text2: TextView = itemview.amountt
        val relatv:ConstraintLayout = itemview.filter1

        fun intiallize(item: CategoriesItem, action:onPostItemClickLisenter){
            relatv.setOnClickListener(){action.onItemClick(item, adapterPosition)}
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSubCategoriesViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.alert_subcategories_cardview,parent,false)
        return AdapterSubCategoriesViewHolder(view)

    }

    override fun getItemCount() = subcateposts.size

    override fun onBindViewHolder(holder: AdapterSubCategoriesViewHolder, position: Int) {


        holder.text1.text  = subcateposts[position].name

        holder.intiallize(subcateposts.get(position),clicklistenersubcate)
    }


    interface onPostItemClickLisenter{
        fun onItemClick(item: CategoriesItem, adapterPosition: Int)
    }


}