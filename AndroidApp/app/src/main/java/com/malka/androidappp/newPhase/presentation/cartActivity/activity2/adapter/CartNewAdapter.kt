package com.malka.androidappp.newPhase.presentation.cartActivity.activity2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemCartDesignNewBinding
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malka.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malka.androidappp.newPhase.presentation.cartActivity.activity1.adapter.CartAdapter
import kotlinx.android.synthetic.main.item_cart_design_new.view.*


class CartNewAdapter(
    var productsCartListResp: List<CartProductDetails>,
    var setProductCartListeners: SetProductNewCartListeners
) : RecyclerView.Adapter<CartNewAdapter.CartNewViewHolder>() {
    lateinit var context: Context

    class CartNewViewHolder(var viewBinding: ItemCartDesignNewBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartNewViewHolder {
        context = parent.context
        return CartNewViewHolder(
            ItemCartDesignNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = productsCartListResp.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartNewViewHolder, position: Int) {
        holder.itemView.tvSellerName.text = productsCartListResp[position].providerName ?: ""
        setAdapter(holder.viewBinding.rvCart, productsCartListResp[position].listProduct,position)
        if (productsCartListResp[position].couponAppliedBussinessAccountDto != null)
        {
            holder.viewBinding.tvTotalPrice.text = "${productsCartListResp[position].couponAppliedBussinessAccountDto!!.finalTotalPriceForBusinessAccount} ${context.getString(R.string.Rayal)}"
            if (productsCartListResp[position].couponAppliedBussinessAccountDto!!.businessAccountAmountBeforeCoupon
                == productsCartListResp[position].couponAppliedBussinessAccountDto!!.businessAccountAmountAfterCoupon
            ) {
                holder.viewBinding.tvOldTotalPrice.hide()
            }else{
                holder.viewBinding.tvOldTotalPrice.show()
                holder.viewBinding.tvOldTotalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.viewBinding.tvOldTotalPrice.text= "${productsCartListResp[position].couponAppliedBussinessAccountDto!!.businessAccountAmountBeforeCoupon} ${context.getString(R.string.Rayal)}"
            }
            if(productsCartListResp[position].couponAppliedBussinessAccountDto!!.businessAccountId!=null){
                holder.viewBinding.couponContainer.show()
                holder.viewBinding.btnApplyCode.setOnClickListener {
                    if(holder.viewBinding.etCoupon.text.toString().trim()==""){
                        holder.viewBinding.etCoupon.error=context.getString(R.string.enter_the_coupon)
                    }else{
                        setProductCartListeners.onApplyBusinessCardCoupon(position,
                            productsCartListResp[position].couponAppliedBussinessAccountDto!!.businessAccountId!!,
                            holder.viewBinding.etCoupon.text.toString().trim()
                        )
                    }

                }
            }else{
                holder.viewBinding.couponContainer.hide()
            }
        } else {
            holder.viewBinding.couponContainer.hide()
            holder.viewBinding.tvOldTotalPrice.hide()
            holder.viewBinding.tvTotalPrice.text ="0${context.getString(R.string.Rayal)}"
        }


//        var productItem: ProductCartItem? = productsCartListResp[position].listProduct?.get(0)
//        if (productItem != null) {
//            Extension.loadThumbnail(
//                context,
//                productItem.img ?: "",
//                holder.viewBinding.ivProductImage,
//                holder.viewBinding.loader
//            )
//            holder.viewBinding.tvProductType.text = productItem.categoryName ?: ""
//            holder.viewBinding.tvProductName.text = productItem.name ?: ""
//            var location = ""
//            if (productItem.country != null) {
//                location += productItem.country
//            }
//            if (productItem.region != null) {
//                location += "-${productItem.region}"
//            }
//            holder.viewBinding.tvProductCity.text = location
//            if (productItem.priceDiscount == productItem.price) {
//                holder.viewBinding.prodPrice.text =
//                    "${productItem.price.toDouble()} ${
//                        context.getString(
//                            R.string.Rayal
//                        )
//                    }"
//                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.hide()
//            } else {
//                // for Horizental View
//                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.show()
//                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.paintFlags =
//                    Paint.STRIKE_THRU_TEXT_FLAG
//                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.text =
//                    "${productItem.price.toDouble()} ${
//                        context.getString(
//                            R.string.Rayal
//                        )
//                    }"
//                holder.viewBinding.prodPrice.text =
//                    "${productItem.priceDiscount.toDouble()} ${
//                        context.getString(
//                            R.string.Rayal
//                        )
//                    }"
//            }
//            holder.viewBinding.tvQuentitiy.text = productItem.qty.toString()
//
//            holder.viewBinding.btnSubtract.setOnClickListener {
//                if (productItem.qty > 1) {
//                    setProductCartListeners.onDecreaseQuantityProduct(position)
//                }
//
//            }
//            holder.viewBinding.btnAdd.setOnClickListener {
//                setProductCartListeners.onIncreaseQuantityProduct(position)
//            }
//            holder.viewBinding.btnDelete.setOnClickListener {
//
//            }
//        }

    }

    private fun setAdapter(rvCart: RecyclerView, listProduct: List<ProductCartItem>?,mainPosition:Int) {
        var dataList: ArrayList<ProductCartItem> = ArrayList()
        listProduct?.let { dataList.addAll(it) }
        var cartAdapter = CartAdapter(dataList, object : CartAdapter.SetProductCartListeners {
            override fun onIncreaseQuantityProduct(position: Int) {
                setProductCartListeners.onIncreaseQuantityProduct(position,mainPosition)
            }

            override fun onDecreaseQuantityProduct(position: Int) {
                setProductCartListeners.onDecreaseQuantityProduct(position,mainPosition)
            }

            override fun onDeleteProduct(position: Int) {
                setProductCartListeners.onDeleteProduct(position,mainPosition)
            }

        })
        rvCart.apply {
            adapter = cartAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled = false
        }
    }

    interface SetProductNewCartListeners {
        fun onIncreaseQuantityProduct(position: Int,mainPosition:Int)
        fun onDecreaseQuantityProduct(position: Int,mainPosition:Int)
        fun onDeleteProduct(position: Int,mainPosition:Int)

        fun onApplyBusinessCardCoupon(mainPosition: Int,businessAccountId:String,coupon:String)
    }
}