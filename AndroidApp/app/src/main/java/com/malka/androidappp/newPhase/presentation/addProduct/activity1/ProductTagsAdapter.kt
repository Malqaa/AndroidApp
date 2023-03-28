package com.malka.androidappp.newPhase.presentation.addProduct.activity1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemSuggestedCategoriesBinding
import com.malka.androidappp.newPhase.domain.models.productTags.Tags


class ProductTagsAdapter(var data:List<SearchTagItem>,var setOnSelectedListeners:SetOnSelectedListeners) :RecyclerView.Adapter<ProductTagsAdapter.ProductTagsViewHolder>() {
    lateinit var context:Context

    class ProductTagsViewHolder(var viewBinding:ItemSuggestedCategoriesBinding)  : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductTagsViewHolder {
        context = parent.context
        return ProductTagsAdapter.ProductTagsViewHolder(
            ItemSuggestedCategoriesBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int=data.size

    override fun onBindViewHolder(holder: ProductTagsViewHolder, position: Int) {
      val currentPosition=position
        holder.viewBinding.categoryNameTv.text=data[position].title ?:""
        holder.viewBinding.ivCheck.setOnClickListener {
            setOnSelectedListeners.onSelectTagItem(
                                currentPosition)
        }
        if(data[position].isSelect){
            holder.viewBinding.ivCheck.setImageDrawable(context.getDrawable(R.drawable.ic_radio_button_checked))
        }else{
            holder.viewBinding.ivCheck.setImageDrawable(context.getDrawable(R.drawable.ic_radio_button_unchecked))
        }
     //
    //   holder.viewBinding.radioButtonSelect.setOnCheckedChangeListener { p0, selectedStatus ->
        //            setOnSelectedListeners.onSelectTagItem(
        //                currentPosition,
        //                selectedStatus
        //            )
        //        }
    //
    //
    //   holder.viewBinding.radioButtonSelect.setChecked(  data[position].isSelect)

    }

    interface SetOnSelectedListeners{
        fun onSelectTagItem(position:Int)
    }
}