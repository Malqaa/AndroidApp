package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.ItemImageForProductDetailsBinding
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show


class ProductImagesAdapter(
    var productImagesList: ArrayList<ImageSelectModel>,
    var setOnSelectedImage: SetOnSelectedImage
) :
    RecyclerView.Adapter<ProductImagesAdapter.ProductImagesViewHolder>() {
    lateinit var context: Context

    class ProductImagesViewHolder(var viewBinding: ItemImageForProductDetailsBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImagesViewHolder {
        context = parent.context
        return ProductImagesViewHolder(
            ItemImageForProductDetailsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = productImagesList.size

    override fun onBindViewHolder(holder: ProductImagesViewHolder, position: Int) {
        if (productImagesList[position].type == 2) {
            holder.viewBinding.loader.hide()
            holder.viewBinding.isVideoIv.show()
        } else {
            holder.viewBinding.isVideoIv.hide()
            Extension.loadImgGlide(
                context,
                productImagesList[position].url,
                holder.viewBinding.productImage,
                holder.viewBinding.loader
            )

        }
        holder.viewBinding.imageLayout.setOnClickListener {
            setOnSelectedImage.onSelectImage(position)
        }
    }

    interface SetOnSelectedImage {
        fun onSelectImage(position: Int)
    }
}