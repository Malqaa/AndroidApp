package com.malka.androidappp.activities_main.add_product.all_categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import kotlinx.android.synthetic.main.all_categories_card.view.*

class AdapterSubCategories(
    private val allSubCategories: List<Category>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterSubCategories.AdapterSubCategoriesViewHolder>() {

    inner class AdapterSubCategoriesViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {
        val categoryName: TextView = itemview.categoryName
        val categoryIcon: ImageView = itemview.arrowimgmobile
        val not_category_iv: ImageView = itemview.not_category_iv

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

        holder.categoryName.text = allSubCategories[position].name

        if (allSubCategories[position].isCategory) {
            holder.categoryIcon.show()
            holder.not_category_iv.hide()
        } else {
            holder.categoryIcon.hide()
            holder.not_category_iv.show()
        }
    }

    interface OnItemClickListener {
        fun OnItemClickHandler(position: Int) {}
    }

}