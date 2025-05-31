package com.malqaa.androidappp.newPhase.presentation.fragments.categoriesFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.utils.Extension

class CategoryAdapter(
    private val categoryList: List<Category>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_category,
                parent,
                false
            )  // R.layout.item_category is the layout you provided
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categoryList.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryNameTv: TextView = itemView.findViewById(R.id.category_name_tv)
        private val categoryImage: ImageView = itemView.findViewById(R.id.category_image)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(category: Category) {
            categoryNameTv.text = category.name

            if (category.image.isEmpty()) {
                categoryImage.setImageResource(R.drawable.product_attribute_bg2)
            } else {
                Extension.loadImgGlide(
                    itemView.context,
                    category.image,
                    categoryImage, progressBar
                )
            }

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(category)
            }
        }
    }
}
