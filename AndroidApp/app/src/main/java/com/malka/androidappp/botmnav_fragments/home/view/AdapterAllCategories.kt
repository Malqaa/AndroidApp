package com.malka.androidappp.botmnav_fragments.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import kotlinx.android.synthetic.main.all_categories_cardview.view.*

class AdapterAllCategories(
    val allCategories: ArrayList<AllCategoriesModel>,
    var context: HomeFragment,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterAllCategories.AdapterAllCategoriesViewHolder>() {

    inner class AdapterAllCategoriesViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {

        val categoryIcon: ImageView = itemview.category_icon
        val categoryName: TextView = itemview.category_name

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
    ): AdapterAllCategoriesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_categories_cardview, parent, false)
        return AdapterAllCategoriesViewHolder(view)

    }

    override fun getItemCount() = allCategories.size

    override fun onBindViewHolder(holder: AdapterAllCategoriesViewHolder, position: Int) {

       // holder.categoryIcon.setImageResource(R.drawable.fashion)
        holder.categoryName.text = allCategories[position].categoryName

    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int) {}
    }

}