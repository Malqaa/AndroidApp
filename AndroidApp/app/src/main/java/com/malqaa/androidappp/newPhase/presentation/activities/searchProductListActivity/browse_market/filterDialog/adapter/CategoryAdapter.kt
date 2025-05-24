package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.filterDialog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category

class CategoryAdapter(
    private val categories: MutableList<Category>,
    private val onCategorySelectedListener: OnCategorySelectedListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class CategoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val itemLayout: LinearLayout = itemView.findViewById(R.id.item_layout)
        val nameTextView: TextView = view.findViewById(R.id.categoryName)
        val expandIcon: ImageView = view.findViewById(R.id.expandIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_1, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.nameTextView.text = category.name

        // Apply selection color logic
        val selectedColor = ContextCompat.getColor(holder.itemView.context, R.color.colorPrimary)
        val deselectedColor = ContextCompat.getColor(holder.itemView.context, R.color.gray)
        holder.nameTextView.setTextColor(if (position == selectedPosition) selectedColor else deselectedColor)
        val iconDrawable = holder.expandIcon.drawable?.mutate()
        iconDrawable?.setTint(if (position == selectedPosition) selectedColor else deselectedColor)
        holder.expandIcon.setImageDrawable(iconDrawable)

        // Expand/collapse logic
        if (category.list.isNotEmpty()) {
            holder.expandIcon.visibility = View.VISIBLE
            val rotationAngle = if (category.isSelect) 90f else 0f
            holder.expandIcon.animate().rotation(rotationAngle).setDuration(200).start()
        } else {
            holder.expandIcon.visibility = View.GONE
        }

        // Click listener
        holder.itemView.setOnClickListener {
            if (category.list.isNotEmpty()) {
                category.isSelect = !category.isSelect
                handleExpansionCollapse(position, category)
            } else {
                handleItemSelected(holder, category, position)
            }
        }
    }

    private fun handleItemSelected(
        holder: CategoryViewHolder,
        category: Category,
        position: Int
    ) {
        // Deselect previous selection
        val previousPosition = selectedPosition
        if (previousPosition != RecyclerView.NO_POSITION) {
            categories[previousPosition].isSelected = false
            notifyItemChanged(previousPosition)
        }

        // Select new item
        selectedPosition = if (selectedPosition == position) {
            RecyclerView.NO_POSITION  // Deselect if same item clicked again
        } else {
            category.isSelected = true
            position
        }
        notifyItemChanged(selectedPosition)

        onCategorySelectedListener.onCategorySelected(category)
    }

    override fun getItemCount(): Int = categories.size

    private fun handleExpansionCollapse(position: Int, category: Category) {
        val currentIndex = position + 1

        if (category.isSelect) {
            categories.addAll(currentIndex, category.list)
            notifyItemRangeInserted(currentIndex, category.list.size)
        } else {
            val count = removeNestedSubcategories(position)
            if (count > 0) {
                notifyItemRangeRemoved(currentIndex, count)
            }
        }
        notifyItemChanged(position)
    }

    private fun removeNestedSubcategories(position: Int): Int {
        var count = 0
        val startIndex = position + 1

        while (startIndex < categories.size) {
            val subcategory = categories[startIndex]
            if (!isNestedSubcategory(subcategory, categories[position])) {
                break
            }
            categories.removeAt(startIndex)
            count++
        }
        return count
    }

    private fun isNestedSubcategory(subcategory: Category, parentCategory: Category): Boolean {
        var list = parentCategory.list
        while (list.isNotEmpty()) {
            if (list.contains(subcategory)) return true
            list = list.flatMap { it.list }
        }
        return false
    }

    fun reset() {
        selectedPosition = RecyclerView.NO_POSITION
        categories.forEachIndexed { index, category ->
            category.isSelected = false
            category.isSelect = false
            notifyItemChanged(index)
        }
    }
}

interface OnCategorySelectedListener {
    fun onCategorySelected(category: Category)
}
