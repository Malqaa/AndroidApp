package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemSuggestedCategoriesBinding
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category

class ProductTagsAdapter(
    var data: ArrayList<Category>,
    var setOnSelectedListeners: SetOnSelectedListeners
) : RecyclerView.Adapter<ProductTagsAdapter.ProductTagsViewHolder>() {
    lateinit var context: Context

    class ProductTagsViewHolder(var viewBinding: ItemSuggestedCategoriesBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductTagsViewHolder {
        context = parent.context
        return ProductTagsAdapter.ProductTagsViewHolder(
            ItemSuggestedCategoriesBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductTagsViewHolder, position: Int) {
        val currentPosition = position
        holder.viewBinding.categoryNameTv.text = data[position].category ?: ""
        holder.viewBinding.ivCheck.setOnClickListener {
            setOnSelectedListeners.onSelectTagItem(
                currentPosition
            )
        }
        if (data[position].isSelect) {
            holder.viewBinding.ivCheck.setImageDrawable(context.getDrawable(R.drawable.ic_radio_button_checked))
        } else {
            holder.viewBinding.ivCheck.setImageDrawable(context.getDrawable(R.drawable.ic_radio_button_unchecked))
        }

    }

    interface SetOnSelectedListeners {
        fun onSelectTagItem(position: Int)
    }
}