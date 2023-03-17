package com.malka.androidappp.newPhase.presentation.adapterShared

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ProductItemBinding
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper


class ProductHorizontalAdapter :
    RecyclerView.Adapter<ProductHorizontalAdapter.SellerProductViewHolder>() {
    lateinit var context: Context

    class SellerProductViewHolder(var viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductViewHolder {
        context = parent.context
        return SellerProductViewHolder(
            ProductItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: SellerProductViewHolder, position: Int) {
        val params: ViewGroup.LayoutParams = holder.viewBinding.fullview.layoutParams
        params.width = context.resources.getDimension(R.dimen._220sdp).toInt()
        params.height = params.height
        holder.viewBinding.fullview.layoutParams = params
        if (Paper.book().read<Boolean>(SharedPreferencesStaticClass.islogin) == true) {

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
    }
}