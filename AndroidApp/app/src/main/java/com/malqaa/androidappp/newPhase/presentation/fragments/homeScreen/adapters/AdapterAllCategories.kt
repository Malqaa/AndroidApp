package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemCategoriesCardInHomeBinding
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.utils.Extension


class AdapterAllCategories(
    private val allCategories: List<Category>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterAllCategories.CategoryViewHolder>() {

    // ViewHolder inner class
    inner class CategoryViewHolder(private val binding: ItemCategoriesCardInHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, position: Int) {
            binding.categoryNameTv.text = category.name
            if (category.image.isNullOrEmpty()) {
                binding.categoryIcon.setImageResource(R.drawable.product_attribute_bg2)
            } else {
                Extension.loadImgGlide(
                    binding.root.context,
                    category.image,
                    binding.categoryIcon,
                    binding.progressBar
                )
            }
            binding.root.setOnClickListener {
                listener.pnCategorySelected(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoriesCardInHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(allCategories[position], position)
    }

    override fun getItemCount() = allCategories.size

    interface OnItemClickListener {
        fun pnCategorySelected(position: Int)
    }
}
