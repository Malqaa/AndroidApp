package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.helper.BaseViewHolder
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import kotlinx.android.synthetic.main.item_categories_card_in_home.view.*


class AdapterAllCategories(
    private val allCategories: List<Category>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categories_card_in_home, parent, false)
        return BaseViewHolder(view)

    }

    override fun getItemCount() = allCategories.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.view.run {
            allCategories[position].run {
                category_name_tv.text = name
                if (image.isNullOrEmpty()) {
                    category_icon.setImageResource(R.drawable.product_attribute_bg2)
                } else {
                    Extension.loadThumbnail(
                        context,
                        image,
                        category_icon, progressBar
                    )
                }
                setOnClickListener {
                    listener.pnCategorySelected(position)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun pnCategorySelected(position: Int) {}
    }

}