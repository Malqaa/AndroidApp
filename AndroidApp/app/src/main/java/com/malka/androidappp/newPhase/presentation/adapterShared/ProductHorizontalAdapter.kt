package com.malka.androidappp.newPhase.presentation.adapterShared

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ProductItemBinding
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.Extension.decimalNumberFormat
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.invisible
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.product_item.view.*


class ProductHorizontalAdapter(var productList:List<Product>) : RecyclerView.Adapter<ProductHorizontalAdapter.SellerProductViewHolder>() {
    lateinit var context: Context

    class SellerProductViewHolder(var viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductViewHolder {
        context = parent.context
        return SellerProductViewHolder(
            ProductItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: SellerProductViewHolder, position: Int) {
        val params: ViewGroup.LayoutParams = holder.viewBinding.fullview.layoutParams
        params.width = context.resources.getDimension(R.dimen._220sdp).toInt()
        params.height = params.height
        holder.viewBinding.fullview.layoutParams = params


        if (HelpFunctions.isUserLoggedIn()) {
            if (productList[position].isFavourite) {
                holder.viewBinding.ivFav.setImageResource(R.drawable.starcolor)
            } else {
                holder.viewBinding.ivFav.setImageResource(R.drawable.star)
            }
        } else {
            holder.viewBinding.ivFav.setImageResource(R.drawable.star)
        }

        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            holder.viewBinding.containerTimeBar.background =
                ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_ar)
        } else {
            holder.viewBinding.containerTimeBar.background =
                ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_en)
        }
        holder.viewBinding.titlenamee.text = productList[position].name?:""
        holder.viewBinding.cityTv.text = productList[position].regionName?:""
        Extension.loadThumbnail(
            context,
            productList[position].productImage?:"",
            holder.viewBinding. productimg,
            holder.viewBinding.loader
        )
        holder.viewBinding.LowestPriceLayout.hide()
        holder.viewBinding.LowestPriceLayout2.hide()
        holder.viewBinding.lisView.hide()

        holder.viewBinding.productPrice.text = "${productList[position].price.toDouble().decimalNumberFormat()} ${
            context.getString(
                R.string.Rayal
            )
        }"
        holder.viewBinding.purchasingPriceTv2.text = "${productList[position].price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"



    }
}