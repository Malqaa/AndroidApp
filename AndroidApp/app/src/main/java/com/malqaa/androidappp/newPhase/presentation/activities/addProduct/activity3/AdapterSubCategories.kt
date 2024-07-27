package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity3

import android.os.Handler
import android.os.Looper
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
import kotlinx.android.synthetic.main.all_categories_card.view.arrowimgmobile
import kotlinx.android.synthetic.main.all_categories_card.view.categoryName
import kotlinx.android.synthetic.main.all_categories_card.view.not_category_iv

class AdapterSubCategories(
    private var allSubCategories: List<Category>,
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

        // Hide the arrow for the last subcategory
        if (allSubCategories[position].list.isEmpty()) {
            holder.categoryIcon.hide()
        } else {
            holder.categoryIcon.show()
        }
    }

    fun updateAdapter(allSubCategories: List<Category>) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            if (allSubCategories.isNotEmpty()) {
                this.allSubCategories = allSubCategories
                notifyDataSetChanged()
            }
        }

    }

    interface OnItemClickListener {
        fun OnItemClickHandler(position: Int) {}
    }

}