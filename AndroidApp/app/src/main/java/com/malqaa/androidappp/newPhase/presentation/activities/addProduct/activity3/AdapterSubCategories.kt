package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class AdapterSubCategories(
    private var allSubCategories: List<Category>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterSubCategories.SubCategoryViewHolder>() {

    inner class SubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryIcon: ImageView = itemView.findViewById(R.id.arrowimgmobile)
        val notCategoryIcon: ImageView = itemView.findViewById(R.id.not_category_iv)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClickHandler(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_categories_card, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun getItemCount(): Int = allSubCategories.size

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val category = allSubCategories[position]
        holder.categoryName.text = category.name

        // Hide the arrow for the last subcategory
        if (category.list.isEmpty()) {
            holder.categoryIcon.hide()
        } else {
            holder.categoryIcon.show()
        }
    }

    fun updateAdapter(allSubCategories: List<Category>) {
        if (allSubCategories.isNotEmpty()) {
            this.allSubCategories = allSubCategories
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClickHandler(position: Int)
    }
}
