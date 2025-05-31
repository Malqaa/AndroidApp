package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity.browse_market.popup_subcategories_list

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.AlertSubcategoriesCardviewBinding
import com.malqaa.androidappp.newPhase.domain.models.categoryResp.CategoriesItem

class AdapterSubCategories(
    private val subcateposts: List<CategoriesItem>,
    var clicklistenersubcate: onPostItemClickLisenter
) : RecyclerView.Adapter<AdapterSubCategories.AdapterSubCategoriesViewHolder>(), Parcelable {

    class AdapterSubCategoriesViewHolder(val binding: AlertSubcategoriesCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun initialize(item: CategoriesItem, action: onPostItemClickLisenter) {
            binding.filter1.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    constructor(parcel: Parcel) : this(
        TODO("subcateposts"),
        TODO("clicklistenersubcate")
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSubCategoriesViewHolder {
        val binding = AlertSubcategoriesCardviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AdapterSubCategoriesViewHolder(binding)
    }

    override fun getItemCount() = subcateposts.size

    override fun onBindViewHolder(holder: AdapterSubCategoriesViewHolder, position: Int) {
        val item = subcateposts[position]

        // Access views through the binding object
        holder.binding.carr.text = item.name
        holder.initialize(item, clicklistenersubcate)
    }

    interface onPostItemClickLisenter {
        fun onItemClick(item: CategoriesItem, adapterPosition: Int)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        // Implement parcelable logic if needed
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdapterSubCategories> {
        override fun createFromParcel(parcel: Parcel): AdapterSubCategories {
            return AdapterSubCategories(parcel)
        }

        override fun newArray(size: Int): Array<AdapterSubCategories?> {
            return arrayOfNulls(size)
        }
    }
}
