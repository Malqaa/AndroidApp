package com.malka.androidappp.newPhase.presentation.cartActivity.activity1.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemCartSellNewBinding
import com.malka.androidappp.newPhase.data.helper.CommonBottomSheet
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.cartListResp.CouponAppliedBussinessAccountDto
import com.malka.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData

class DetailCartAdapter(
//    val providerId:String,
//    val coupon: CouponAppliedBussinessAccountDto,
    var listProduct: ArrayList<ProductCartItem>,
    var setProductCartListeners: SetProductCartListeners,
    private val deliveryOptionList: ArrayList<Selection> = ArrayList(),
    private val paymentMethodList: ArrayList<Selection> = ArrayList(),

    ) :
    RecyclerView.Adapter<DetailCartAdapter.CartViewHolder>() {
    lateinit var context: Context

    class CartViewHolder(var viewBinding: ItemCartSellNewBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        context = parent.context
        return CartViewHolder(
            ItemCartSellNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Extension.loadThumbnail(
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
        holder.viewBinding.tvQuentitiy.text = listProduct[position].cartProductQuantity.toString()

        holder.viewBinding.btnSubtract.setOnClickListener {
            if ((listProduct[position].cartProductQuantity?:0) > 1) {
                setProductCartListeners.onDecreaseQuantityProduct(position)
            }

        }
        holder.viewBinding.btnAdd.setOnClickListener {
            if (holder.viewBinding.tvQuentitiy.text.toString().toInt() < (listProduct[position].qty?:0))
                setProductCartListeners.onIncreaseQuantityProduct(position)
            else
                HelpFunctions.ShowLongToast(
                    holder.viewBinding.tvQuentitiy.context.getString(R.string.QuentitiyClosed),
                    holder.viewBinding.tvQuentitiy.context
                )
        }

        holder.viewBinding.btnDelete.setOnClickListener {
            setProductCartListeners.onDeleteProduct(position)
        }



        holder.viewBinding.deliveryOption.setOnClickListener {
            if (listProduct[position].shippingOptions.isNotEmpty()) {
                val shipList: ArrayList<Selection> =
                    listProduct[position].shippingOptions.map { ship ->
                        Selection(id = ship.id, name = ship.shippingOptionId.toString())
                    }.toCollection(ArrayList())

                shipList.forEachIndexed { index, element ->
                    when (element.name) {
                        AddProductObjectData.ShippingType.FreeShippingWithinSaudiArabia.value.toString() -> {
                            shipList[index].name= AddProductObjectData.ShippingType.FreeShippingWithinSaudiArabia.name
                        }
                        AddProductObjectData.ShippingType.IntegratedShippingCompanyOptions.value.toString() -> {
                            shipList[index].name= AddProductObjectData.ShippingType.IntegratedShippingCompanyOptions.name
                        }
                        AddProductObjectData.ShippingType.ArrangementWillBeMadeWithTheBuyer.value.toString() -> {
                            shipList[index].name= AddProductObjectData.ShippingType.ArrangementWillBeMadeWithTheBuyer.name
                        }

                    }

                }

                deliveryOptionList.apply {
                    clear()
                    addAll(shipList)
                }
                CommonBottomSheet().commonSelctinDialog(
                    context,
                    deliveryOptionList, context.getString(R.string.delivery_options)
                ) {
                    holder.viewBinding.deliveryOption.text = it.name
                    setProductCartListeners.onSelectDelivery(
                        listProduct[position].id,
                        it.id.toString()
                    )

                }
            }
        }


        holder.viewBinding.paymentOptionBtn.setOnClickListener {
            if (listProduct[position].paymentOptions.isNotEmpty()) {
                val paymentList: ArrayList<Selection> =
                    listProduct[position].paymentOptions.map { ship ->
                        Selection(id = ship.id, name = ship.paymentOptionId.toString())
                    }.toCollection(ArrayList())

                paymentList.forEachIndexed { index, element ->
                    when (element.name) {
                        AddProductObjectData.PaymentType.Cash.value.toString() -> {
                            paymentList[index].name= AddProductObjectData.PaymentType.Cash.name
                        }
                        AddProductObjectData.PaymentType.BankTransfer.value.toString() -> {
                            paymentList[index].name= AddProductObjectData.PaymentType.BankTransfer.name
                        }
                        AddProductObjectData.PaymentType.CreditCard.value.toString() -> {
                            paymentList[index].name= AddProductObjectData.PaymentType.CreditCard.name
                        }
                        AddProductObjectData.PaymentType.Mada.value.toString() -> {
                            paymentList[index].name=AddProductObjectData.PaymentType.Mada.name
                        }
                    }

                }

                paymentMethodList.apply {
                    clear()
                    addAll(paymentList)
                }

                CommonBottomSheet().commonSelctinDialog(
                    context,
                    paymentMethodList, context.getString(R.string.PaymentOptions)
                ) {
                    holder.viewBinding.paymentOptionBtn.text = it.name
                    setProductCartListeners.onSelectPayment(listProduct[position].id, it.id)

                }
            }
        }

    }

    interface SetProductCartListeners {
        fun onIncreaseQuantityProduct(position: Int)
        fun onDecreaseQuantityProduct(position: Int)
        fun onDeleteProduct(position: Int)

        fun onSelectPayment(productId: Int, paymentSelection: Int)
        fun onSelectDelivery(productId: Int, deliverySelection: String)
//        fun onApplyBusinessCardCoupon(mainPosition: Int, businessAccountId: String, coupon: String,providerId:String)
    }

}