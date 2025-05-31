package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class AdapterAllCategoriesAdapter(
    private var allCategories: List<Category>,
    var onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<AdapterAllCategoriesAdapter.CategoryViewHolder>() {

    // ViewHolder class
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.category_name_tv)
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        val bgLine: View =
            itemView.findViewById(R.id.bgline) // Assuming there's a view with this ID
        val isSelectImage: View =
            itemView.findViewById(R.id.is_selectimage) // Assuming there's a view with this ID

        // Optionally, you can create a method to bind data to views
        fun bind(category: Category) {
            categoryNameTextView.text = category.name

            if (category.is_select) {
                AddProductObjectData.selectedCategoryId = category.id
                isSelectImage.show()
                // Set stroke color, show/hide views as necessary
            } else {
                bgLine.hide()
                isSelectImage.hide()
                // Set stroke color, show/hide views as necessary
            }

            if (category.image.isNullOrEmpty()) {
                categoryIcon.setImageResource(R.mipmap.ic_launcher)
            } else {
                Glide.with(itemView.context)
                    .load(category.image)
                    .error(R.mipmap.ic_launcher_round)
                    .into(categoryIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_categories_cardview, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount() = allCategories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = allCategories[position]
        holder.bind(category) // Bind data to the view

        holder.itemView.setOnClickListener {
            // Reset selection
            allCategories.forEach { it.is_select = false }
            category.is_select = true // Set the selected category
            notifyDataSetChanged() // Notify the adapter of data change
            onItemClick.invoke(position) // Invoke the click callback
        }
    }
}
