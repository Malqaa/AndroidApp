package com.malka.androidappp.botmnav_fragments.my_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.CategoryTagsModel
import com.malka.androidappp.botmnav_fragments.create_ads.ChooseCateFragment
import com.malka.androidappp.botmnav_fragments.create_ads.Data
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_product_cardview.view.*
import kotlinx.android.synthetic.main.suggested_categories.view.*


class AdapterSuggestedCategories(
    val suggestedCategories: ArrayList<Data>,
    var context: ChooseCateFragment,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterSuggestedCategories.AdapterSuggestedCategoriesViewHolder>() {


    inner class AdapterSuggestedCategoriesViewHolder(itemview: View) :
        RecyclerView.ViewHolder(itemview),
        View.OnClickListener {
        val suggestedCategoriesTitle: TextView = itemview.suggested_category_title
        val suggestedCategoriesPath: TextView = itemview.category_path

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.OnItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSuggestedCategoriesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.suggested_categories, parent, false)
        return AdapterSuggestedCategoriesViewHolder(view)

    }

    override fun getItemCount() = suggestedCategories.size

    override fun onBindViewHolder(holder: AdapterSuggestedCategoriesViewHolder, position: Int) {

        holder.suggestedCategoriesTitle.text = suggestedCategories[position].name
        holder.suggestedCategoriesPath.text = suggestedCategories[position].path
    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int) {}
    }

}

