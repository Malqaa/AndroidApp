package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemCartDesignNewBinding
import com.malqaa.androidappp.newPhase.domain.enums.ShippingType
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CouponAppliedBussinessAccountDto
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.adapter.DetailCartAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class CartNewAdapter(
    private val flagTypeSale: Boolean,
    private var productsCartListResp: List<CartProductDetails>,
    private var setProductCartListeners: SetProductNewCartListeners,
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

    fun updateAdapter(productsCartListResp: ArrayList<CartProductDetails>) {
        this.productsCartListResp = productsCartListResp
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = productsCartListResp.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartNewViewHolder, position: Int) {
        val productDetails = productsCartListResp[position]

        // Access views via viewBinding, not itemView
        if (flagTypeSale) {
            holder.viewBinding.laySelectBank.visibility = View.GONE
            holder.viewBinding.couponContainer.visibility = View.VISIBLE
        }

        holder.viewBinding.tvSellerName.text = productDetails.businessAccountName
            ?: productDetails.providerName ?: ""

        holder.viewBinding.tvDeleteShipping.setOnClickListener {
            setProductCartListeners.onDeleteShipping(position)
        }

        // Set up the nested adapter
        setAdapter(
            productDetails.providerId!!,
            productDetails.couponAppliedBussinessAccountDto!!,
            holder.viewBinding.rvCart,
            productDetails.listProduct,
            position
        )

        // Coupon related UI handling
        val couponDetails = productDetails.couponAppliedBussinessAccountDto
        if (couponDetails != null) {
            holder.viewBinding.tvTotalPrice.text =
                "${couponDetails.finalTotalPriceForBusinessAccount} ${context.getString(R.string.Rayal)}"

            if (couponDetails.businessAccountAmountBeforeCoupon == couponDetails.businessAccountAmountAfterCoupon) {
                holder.viewBinding.tvOldTotalPrice.hide()
            } else {
                holder.viewBinding.tvOldTotalPrice.show()
                holder.viewBinding.tvOldTotalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.viewBinding.tvOldTotalPrice.text =
                    "${couponDetails.businessAccountAmountBeforeCoupon} ${context.getString(R.string.Rayal)}"
            }
        } else {
            holder.viewBinding.tvOldTotalPrice.hide()
            holder.viewBinding.tvTotalPrice.text = "0${context.getString(R.string.Rayal)}"
        }

        // Handle the coupon apply/unapply logic
        if (productDetails.shipmentCouponId != null) {
            holder.viewBinding.btnApplyCode.text = context.getText(R.string.unactivate)
        } else {
            holder.viewBinding.btnApplyCode.text = context.getText(R.string.activation)
        }

        holder.viewBinding.btnApplyCode.setOnClickListener {
            if (holder.viewBinding.etCoupon.text.toString().trim().isEmpty()) {
                holder.viewBinding.etCoupon.error = context.getString(R.string.enter_the_coupon)
            } else {
                val couponText = holder.viewBinding.etCoupon.text.toString().trim()
                if (productDetails.shipmentCouponId != null) {
                    setProductCartListeners.unApplyBusinessCardCoupon(
                        position,
                        productDetails.couponAppliedBussinessAccountDto?.businessAccountId
                            ?: "null",
                        couponText,
                        productDetails.providerId!!
                    )
                    holder.viewBinding.etCoupon.setText("")
                } else {
                    setProductCartListeners.onApplyBusinessCardCoupon(
                        position,
                        productDetails.couponAppliedBussinessAccountDto?.businessAccountId
                            ?: "null",
                        couponText,
                        productDetails.providerId!!
                    )
                }
            }
        }
    }

    private fun setAdapter(
        providerId: String,
        coupon: CouponAppliedBussinessAccountDto,
        rvCart: RecyclerView,
        listProduct: List<ProductCartItem>?,
        mainPosition: Int
    ) {
        val dataList: ArrayList<ProductCartItem> = ArrayList()
        listProduct?.let { dataList.addAll(it) }

        val cartAdapter =
            DetailCartAdapter(dataList, object : DetailCartAdapter.SetProductCartListeners {
                override fun onIncreaseQuantityProduct(position: Int) {
                    setProductCartListeners.onIncreaseQuantityProduct(position, mainPosition)
                }

                override fun onDecreaseQuantityProduct(position: Int) {
                    setProductCartListeners.onDecreaseQuantityProduct(position, mainPosition)
                }

                override fun onDeleteProduct(position: Int) {
                    setProductCartListeners.onDeleteProduct(position, mainPosition)
                }

                override fun onSelectPayment(productId: Int, paymentSelection: Int) {
                    setProductCartListeners.onSelectPayment(productId, paymentSelection)
                }

                override fun onSelectDelivery(productId: Int, shippingType: String,deliverySelection: String) {
                    setProductCartListeners.onSelectDelivery(productId, shippingType,deliverySelection)
                }
            })

        rvCart.apply {
            adapter = cartAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled = false
        }
    }

    interface SetProductNewCartListeners {
        fun onIncreaseQuantityProduct(position: Int, mainPosition: Int)
        fun onDecreaseQuantityProduct(position: Int, mainPosition: Int)
        fun onDeleteProduct(position: Int, mainPosition: Int)
        fun onSelectPayment(productId: Int, paymentSelection: Int)
        fun onSelectDelivery(productId: Int, shippingType: String,deliverySelection: String)
        fun onApplyBusinessCardCoupon(
            mainPosition: Int,
            businessAccountId: String?,
            coupon: String,
            providerId: String
        )

        fun unApplyBusinessCardCoupon(
            mainPosition: Int,
            businessAccountId: String?,
            coupon: String,
            providerId: String
        )

        fun onDeleteShipping(position: Int)
    }
}
