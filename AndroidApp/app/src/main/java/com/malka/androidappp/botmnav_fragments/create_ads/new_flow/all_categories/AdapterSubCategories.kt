package com.malka.androidappp.botmnav_fragments.create_ads.new_flow.all_categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import kotlinx.android.synthetic.main.all_categories_card.view.*

class AdapterSubCategories(
    private val allSubCategories: List<AllCategoriesModel>,
    var context: SubCategories,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterSubCategories.AdapterSubCategoriesViewHolder>() {

    inner class AdapterSubCategoriesViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {
        val categoryName: TextView = itemview.categoryName
        val categoryIcon: ImageView = itemview.arrowimgmobile

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.OnItemClickHandler(position)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSubCategoriesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_categories_card, parent, false)
        return AdapterSubCategoriesViewHolder(view)

    }

    override fun getItemCount() = allSubCategories.size

    override fun onBindViewHolder(holder: AdapterSubCategoriesViewHolder, position: Int) {

        holder.categoryName.text = allSubCategories[position].categoryName

        if (!allSubCategories[position].isCategory) {
            holder.categoryIcon.setImageResource(R.drawable.ic_block)
        } else {
            holder.categoryIcon.setImageResource(R.drawable.arrow_right_black_24dp)
        }
    }

    interface OnItemClickListener {
        fun OnItemClickHandler(position: Int) {}
    }

}