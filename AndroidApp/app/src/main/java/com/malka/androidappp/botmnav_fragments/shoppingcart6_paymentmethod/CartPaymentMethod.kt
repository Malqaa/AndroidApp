package com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shopping_cart2.AdapterShoppingCart2
import com.malka.androidappp.botmnav_fragments.shopping_cart2.ModelShoppingcart2
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.payment_productlist.AdapterPaymentProductList
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.paymentmethod_bankscards.AdapterCartPaymentCard
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.paymentmethod_bankscards.ModelCartPaymentCard
import com.malka.androidappp.botmnav_fragments.shoppingcart6_paymentmethod.payment_productlist.ModelPaymentProductlist
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.servicemodels.user.UserObject
import kotlinx.android.synthetic.main.fragment_cart_payment_method.*
import kotlinx.android.synthetic.main.fragment_shoppingcart2.*
import java.lang.Exception


class CartPaymentMethod : Fragment() {

    var shoppingCartposts: ArrayList<ModelShoppingcart2> = ArrayList()
    var paymentcardsPost: ArrayList<ModelCartPaymentCard> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_payment_method, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_cart_payment.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_cart_payment.setNavigationOnClickListener { requireActivity().onBackPressed() }
        GetUserCreditCards()
        GetUserCartItems()

        /////////////////////////////////////////////////////////////////////////////////////////
        placeorder_btn.setOnClickListener() {
            CheckoutUserCart();
        }

        btn_add_new_card.setOnClickListener() {
            findNavController().navigate(R.id.payment_to_add_new_card)
        }
    }

    fun GetUserCreditCards() {
        try {
            HelpFunctions.GetUserCreditCards(this@CartPaymentMethod);
            BindUserCreditCards();
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindUserCreditCards() {
        try {
            val paymentcardRecycler: RecyclerView =
                requireActivity().findViewById(R.id.recycler_paymentcard);
            paymentcardsPost = ArrayList()

            if (ConstantObjects.usercreditcard != null && ConstantObjects.usercreditcard!!.size > 0) {
                for (IndItem in ConstantObjects.usercreditcard!!) {
                    if (IndItem != null) {
                        paymentcardsPost.add(
                            ModelCartPaymentCard(
                                R.drawable.visa_card,
                                IndItem.cardnumber,
                                id = IndItem.id,
                                selected = IndItem.isSelected
                            )
                        )
                    }
                }
            }
            if (paymentcardsPost == null || paymentcardsPost.size == 0) {
                HelpFunctions.ShowLongToast(
                    "No Record Found",
                    this@CartPaymentMethod.context
                )
            }
            paymentcardRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            paymentcardRecycler.adapter = AdapterCartPaymentCard(paymentcardsPost, this)
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun GetUserCartItems() {
        try {
            HelpFunctions.GetUsersCartList(this@CartPaymentMethod);
            BindUserCartItems();
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindUserCartItems() {
        try {
            val paymentProductListRecycler: RecyclerView =
                requireActivity().findViewById(R.id.payment_productlist_recycler)

            shoppingCartposts = ArrayList()
            if (ConstantObjects.usercart != null && ConstantObjects.usercart!!.size > 0) {
                for (IndItem in ConstantObjects.usercart!!) {
                    if (IndItem != null && IndItem.advertisements != null) {
                        shoppingCartposts.add(
                            ModelShoppingcart2(
                                productImgCaart = if (IndItem.advertisements.image != null) IndItem.advertisements.image.trim() else "",
                                cartproductDescripp = if (IndItem.advertisements.producttitle != null) IndItem.advertisements.producttitle.trim() else "",
                                listingPartyy = if (IndItem.user.username != null) IndItem.user.username.trim() else "",
                                pricebyQuantityy = if (IndItem.advertisements.price != null) IndItem.advertisements.price.trim() else "",
                                totalPrice = if (IndItem.advertisements.price != null) IndItem.advertisements.price.trim() else "",
                                advid = if (IndItem.advertisements.id != null) IndItem.advertisements.id.trim() else "",
                                ItemInWatchlist = HelpFunctions.AdAlreadyAddedToWatchList(IndItem.advertisements.id),
                                CartId = if (IndItem._id != null) IndItem._id.trim() else ""
                            )
                        )
                    }
                }
            }
            CalculateCost();
            if (shoppingCartposts == null || shoppingCartposts.size == 0) {
                HelpFunctions.ShowLongToast(
                    "No Record Found",
                    this@CartPaymentMethod.context
                )
            }
            paymentProductListRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            var browadpt: AdapterShoppingCart2 =
                AdapterShoppingCart2(shoppingCartposts, this@CartPaymentMethod,requireContext())
            paymentProductListRecycler.adapter = browadpt
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun CalculateCost() {
        try {
            var _total_amount: Double = 0.0;
            var _total_qty: Double = 0.0;

            if (ConstantObjects.usercart != null && ConstantObjects.usercart!!.size > 0) {
                for (IndItem in ConstantObjects.usercart!!) {
                    if (IndItem != null && IndItem.advertisements != null) {
                        val indamount =
                            (if (IndItem.advertisements.price != null) IndItem.advertisements.price.trim()
                                .toDouble() else 0.0)
                        val indqty =
                            (if (IndItem.advertisements.qty != null) IndItem.advertisements.qty.trim()
                                .toDouble() else 1.0)

                        _total_amount = _total_amount + (indamount * indqty)
                        _total_qty = _total_qty + _total_qty
                    }
                }
            }

            subtotal_payment.text = "Subtotal (" + ConstantObjects.usercart!!.size + ")"
            subtotal_amount_payment.text = _total_amount.toInt().toString().format(2)
            shippingpayment_label.text = "0"
            //grosstotal_label.text = _total_amount.toString().format(2) + shippingpayment_label.text.toString().format(2)
            grosstotal_label.text = _total_amount.toInt().toString().format(2) + shippingpayment_label.text.toString().format(2)
            //lbl_total_qty.text = _total_qty.toString().format(2)
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun CheckoutUserCart() {
        try {
            if (ConstantObjects.selected_credit_card_index > -1) {
                var cartIds: MutableList<String> = mutableListOf()
                if (ConstantObjects.usercart != null && ConstantObjects.usercart!!.size > 0) {
                    for (IndItem in ConstantObjects.usercart!!) {
                        if (IndItem != null && IndItem.advertisements != null) {
                            cartIds.add(IndItem.advertisements.id)
                        }
                    }
                }
                var checkoutinfo: CheckoutRequestModel = CheckoutRequestModel(
                    cartId = cartIds,
                    addressId = ConstantObjects.useraddresses!![ConstantObjects.selected_address_index].id,
                    tax = "0",
                    totalamount = grosstotal_label.text.toString(),
                    creditCardNo = ConstantObjects.usercreditcard!![ConstantObjects.selected_credit_card_index].cardnumber,
                    loginId = ConstantObjects.logged_userid
                )
                val resp = HelpFunctions.PostUserCheckOut(checkoutinfo, this@CartPaymentMethod);
                if (resp) {
                    findNavController().navigate(R.id.payment_to_checkout)
                } else {
                    HelpFunctions.ShowLongToast(
                        "Error Checking Out",
                        this@CartPaymentMethod.context
                    )
                }
            } else {
                HelpFunctions.ShowLongToast(
                    "Please Select Payment Method",
                    this@CartPaymentMethod.context
                )
            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }
}