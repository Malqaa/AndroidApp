package com.malka.androidappp.botmnav_fragments.shopping_cart2

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.recycler_watchlist.WatchlistAdap
import com.malka.androidappp.recycler_watchlist.WatchlistModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.servicemodels.user.UserObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_shoppingcart2.*
import kotlinx.android.synthetic.main.fragment_watchlist.*
import java.lang.Exception


class Shoppingcart2 : Fragment() {

    var shoppingCartposts: ArrayList<ModelShoppingcart2> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shoppingcart2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_shopcart2.title = "Shopping Cart"
        toolbar_shopcart2.setTitleTextColor(Color.WHITE)
        toolbar_shopcart2.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_shopcart2.setNavigationOnClickListener { requireActivity().onBackPressed() }

        checkout_btn.setOnClickListener() {
            GoToShippingAddress()
        }
        GetUserCartItems()
    }

    fun GoToShippingAddress() {
        try {
            if (ConstantObjects.usercart != null && ConstantObjects.usercart!!.size > 0) {
                findNavController().navigate(R.id.checkout_to_shipping_address)
            } else {
                HelpFunctions.ShowLongToast("Shopping Cart is Empty", this@Shoppingcart2.context)
            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun GetUserCartItems() {
        try {
            HelpFunctions.GetUsersCartList(this@Shoppingcart2);
            BindUserCartItems();
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindUserCartItems() {
        try {
            val shoppingCartRecycler: RecyclerView =
                requireActivity().findViewById(R.id.recyclerViewShopCart)

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
                    this@Shoppingcart2.context
                )
            }
            shoppingCartRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            var browadpt: AdapterShoppingCart2 =
                AdapterShoppingCart2(shoppingCartposts, this@Shoppingcart2)
            shoppingCartRecycler.adapter = browadpt
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

            lbl_subtotal_text.text = "Subtotal (" + ConstantObjects.usercart!!.size + ")"
            lbl_subtotal_amount.text = _total_amount.toString().format(2)
            lbl_shipping_amount.text = "0"
            lbl_gross_total.text =
                _total_amount.toString().format(2) + lbl_shipping_amount.text.toString().format(2)
            lbl_total_qty.text = _total_qty.toString().format(2)
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }
}