package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemProductInCartBinding
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class CartAdapter(
    var listProduct: ArrayList<ProductCartItem>,
    var setProductCartListeners: SetProductCartListeners,

    ) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    lateinit var context: Context

    class CartViewHolder(var viewBinding: ItemProductInCartBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        context = parent.context
        return CartViewHolder(
            ItemProductInCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listProduct.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Extension.loadImgGlide(
            context,
            listProduct[position].img ?: "",
            holder.viewBinding.ivProductImage,
            holder.viewBinding.loader
        )
        holder.viewBinding.tvProductType.text = listProduct[position].categoryName ?: ""
        holder.viewBinding.tvProductName.text = listProduct[position].name ?: ""
        var location = ""
        if (listProduct[position].country != null) {
            location += listProduct[position].country
        }
        if (listProduct[position].region != null) {
            location += "-${listProduct[position].region}"
        }
        holder.viewBinding.tvProductCity.text = location
        if (listProduct[position].priceDiscount == listProduct[position].price) {
            holder.viewBinding.prodPrice.text =
                "${listProduct[position].price.toDouble()} ${
                    context.getString(
                        R.string.Rayal
                    )
                }"
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.hide()
        } else {
            // for Horizental View
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.show()
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.paintFlags =
                Paint.STRIKE_THRU_TEXT_FLAG
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.text =
                "${listProduct[position].price.toDouble()} ${
                    context.getString(
                        R.string.Rayal
                    )
                }"
            holder.viewBinding.prodPrice.text =
                "${listProduct[position].priceDiscount.toDouble()} ${
                    context.getString(
                        R.string.Rayal
                    )
                }"
        }

        holder.viewBinding.tvQuentitiy.text =
            (listProduct[position].cartProductQuantity ?: "0").toString()

        holder.viewBinding.btnSubtract.setOnClickListener {
            if ((listProduct[position].cartProductQuantity ?: 0) > 1) {
                setProductCartListeners.onDecreaseQuantityProduct(position)
            }

        }
        holder.viewBinding.btnAdd.setOnClickListener {
            if (listProduct[position].qty == null) {
                setProductCartListeners.onIncreaseQuantityProduct(position)
            } else {
                if (holder.viewBinding.tvQuentitiy.text.toString()
                        .toInt() < (listProduct[position].qty ?: 0)
                )
                    setProductCartListeners.onIncreaseQuantityProduct(position)
                else
                    HelpFunctions.ShowLongToast(
                        holder.viewBinding.tvQuentitiy.context.getString(R.string.QuentitiyClosed),
                        holder.viewBinding.tvQuentitiy.context
                    )
            }
        }
        holder.viewBinding.btnDelete.setOnClickListener {
            setProductCartListeners.onDeleteProduct(position)
        }


    }

    interface SetProductCartListeners {
        fun onIncreaseQuantityProduct(position: Int)
        fun onDecreaseQuantityProduct(position: Int)
        fun onDeleteProduct(position: Int)

    }

}