package com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shopping_cart2.AdapterShoppingCart2
import com.malka.androidappp.botmnav_fragments.shopping_cart2.ModelShoppingcart2
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.ModelCartShippingAddress
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.adapter_shippingaddress.AdapterCartShipppingAdress
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_productlist.AdapterShippingProductlist
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_productlist.ModelShippingProductlist
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.user.UserObject
import kotlinx.android.synthetic.main.fragment_shipping_address.*
import kotlinx.android.synthetic.main.fragment_shoppingcart2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ShippingAddress : Fragment() {

    var shoppingCartposts: ArrayList<ModelShoppingcart2> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipping_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_shopcart_shipping.title = "Shipping Address"
        toolbar_shopcart_shipping.setTitleTextColor(Color.WHITE)
        toolbar_shopcart_shipping.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_shopcart_shipping.setNavigationOnClickListener { requireActivity().onBackPressed() }

        GetUserCartItems()
        GetUserShippingAddress()
        ///////////////////////////////////////////////////////////////////////////////////////////////
        checkout_btn_shippingcart.setOnClickListener() {
            if (ConstantObjects.selected_address_index > -1) {
                findNavController().navigate(R.id.shipping_address_to_payment)
            } else {
                HelpFunctions.ShowLongToast(
                    "Please Select Shipping Address!",
                    this@ShippingAddress.context
                )
            }
        }
        btn_add_new_address.setOnClickListener() {
            findNavController().navigate(R.id.shippingaddress_googlemap)
        }
    }

    fun GetUserCartItems() {
        try {
            HelpFunctions.GetUsersCartList(this@ShippingAddress);
            BindUserCartItems();
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindUserCartItems() {
        try {
            val shoppingCartRecycler: RecyclerView =
                requireActivity().findViewById(R.id.recylerView_products)
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
                                ItemInWatchlist = HelpFunctions.AdAlreadyAddedToWatchList(
                                    IndItem.advertisements.id
                                ),
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
                    this@ShippingAddress.context
                )
            }
            shoppingCartRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            var browadpt: AdapterShoppingCart2 =
                AdapterShoppingCart2(shoppingCartposts, this@ShippingAddress)
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

            subtotal_cartshipping.text = "Subtotal (" + ConstantObjects.usercart!!.size + ")"
            subtotal_amount.text = _total_amount.toString().format(2)
            lbl_shipping_shipping_amount.text = "0"
            lbl_shipping_gross_amount.text =
                _total_amount.toString()
                    .format(2) + lbl_shipping_shipping_amount.text.toString()
                    .format(2)
            lbl_shipping_total_qty.text = _total_qty.toString().format(2)
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun GetUserShippingAddress() {
        try {
            HelpFunctions.GetUserShippingAddress(this@ShippingAddress);
            BindUserShippingAddresses();
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindUserShippingAddresses() {
        val addressRecycler: RecyclerView =
            requireActivity().findViewById(R.id.recyclerView_address)
        if (ConstantObjects.useraddresses != null && ConstantObjects.useraddresses!!.size > 0) {
            addressRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            addressRecycler.adapter =
                AdapterCartShipppingAdress(
                    ConstantObjects.useraddresses!!,
                    this@ShippingAddress
                )
        } else {
            addressRecycler.adapter = null
            HelpFunctions.ShowLongToast("No Record Found", this@ShippingAddress.context)
        }
    }
}