package com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemImageForProductDetailsBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.productResp.ProductMediaItemDetails


class ProductImagesAdapter(var productImagesList: ArrayList<ProductMediaItemDetails>,var setOnSelectedImage:SetOnSelectedImage) :
    RecyclerView.Adapter<ProductImagesAdapter.ProductImagesViewHolder>(){
    lateinit var context: Context
    class  ProductImagesViewHolder(var viewBinding: ItemImageForProductDetailsBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImagesViewHolder {
        context = parent.context
        return  ProductImagesViewHolder(
            ItemImageForProductDetailsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = productImagesList.size

    override fun onBindViewHolder(holder: ProductImagesViewHolder, position: Int) {

        if (productImagesList[position].type==2) {

            //video
            Extension.loadThumbnail(
                context,
                productImagesList[position].url,
                holder.viewBinding.productImage,
                holder.viewBinding.loader
            )
            holder.viewBinding.isVideoIv.show()
        } else {
            holder.viewBinding.isVideoIv.hide()
            Extension.loadThumbnail(
                context,
                productImagesList[position].url,
                holder.viewBinding.productImage,
                holder.viewBinding.loader
            )

        }
        holder.viewBinding.imageLayout.setOnClickListener {
            setOnSelectedImage.onSelectImage(position)
        }
//        setOnClickListener {
//            if (isVideo) {
//                startActivity(
//                    Intent(
//                        this@ProductDetailsActivity,
//                        PlayActivity::class.java
//                    ).putExtra(
//                        "videourl",
//                        link
//                    )
//                )
//
//            } else {
//                list.forEach {
//                    it.is_select = false
//                }
//                is_select = true
//                rvProductImages.post({ rvProductImages.adapter!!.notifyDataSetChanged() })
//                selectLink = imageURL
//                Extension.loadThumbnail(
//                    this@ProductDetailsActivity,
//                    imageURL,
//                    productimg, loader
//                )
//            }
//
//
//        }
    }

    interface SetOnSelectedImage{
    fun     onSelectImage(position:Int)
    }
}