package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemCartSellNewBinding
import com.malqaa.androidappp.newPhase.domain.enums.PaymentType
import com.malqaa.androidappp.newPhase.domain.enums.ShippingType
import com.malqaa.androidappp.newPhase.utils.helper.CommonBottomSheet
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        if(listProduct[position].msgError=="" || listProduct[position].msgError==null)
            holder.viewBinding.tvError.visibility=View.GONE
        else{
            holder.viewBinding.tvError.visibility=View.VISIBLE
            holder.viewBinding.tvError.text=listProduct[position].msgError
        }
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



        holder.viewBinding.deliveryOption.setOnClickListener {
            if (listProduct[position].shippingOptions.isNotEmpty()) {
                val shipList: ArrayList<Selection> =
                    listProduct[position].shippingOptions.map { ship ->
                        Selection(id = ship.id, name = ship.shippingOptionId.toString())
                    }.toCollection(ArrayList())

                shipList.forEachIndexed { index, element ->
                    when (element.name) {
                        ShippingType.FreeShippingWithinSaudiArabia.value.toString() -> {
                            shipList[index].name =
                                ShippingType.FreeShippingWithinSaudiArabia.name
                        }

                        ShippingType.IntegratedShippingCompanyOptions.value.toString() -> {
                            shipList[index].name =
                                ShippingType.IntegratedShippingCompanyOptions.name
                        }

                        ShippingType.ArrangementWillBeMadeWithTheBuyer.value.toString() -> {
                            shipList[index].name =
                                ShippingType.ArrangementWillBeMadeWithTheBuyer.name
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
                        PaymentType.Cash.value.toString() -> {
                            paymentList[index].name = PaymentType.Cash.name
                        }

                        PaymentType.BankTransfer.value.toString() -> {
                            paymentList[index].name =
                               PaymentType.BankTransfer.name
                        }

                        PaymentType.CreditCard.value.toString() -> {
                            paymentList[index].name =
                                PaymentType.CreditCard.name
                        }

                        PaymentType.Mada.value.toString() -> {
                            paymentList[index].name = PaymentType.Mada.name
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