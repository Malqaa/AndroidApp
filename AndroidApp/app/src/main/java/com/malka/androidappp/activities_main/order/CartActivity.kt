package com.malka.androidappp.activities_main.order

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.addtocart.AddToCartResponseModel
import com.malka.androidappp.servicemodels.addtocart.CartItemModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Response


class CartActivity : BaseActivity() {

    override fun onResume() {
        super.onResume()
        GetUsersCartList {
            setCategoryAdaptor()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbar_title.text = getString(R.string.shopping_basket)
        back_btn.setOnClickListener {
            finish()
        }



        the_next.setOnClickListener {
            if (ConstantObjects.usercart.size > 0) {
                startActivity(Intent(this, AddressPaymentActivity::class.java))
            } else {
                showError(getString(R.string.empty_cart))
            }
        }
    }
    private fun setCategoryAdaptor() {
        ConstantObjects.usercart.forEach {
            it.advertisements.qty = "1"
        }
        getTotalPrice()
        cart_rcv.adapter = object : GenericListAdapter<CartItemModel>(
            R.layout.cart_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.advertisements.run {
                        //  prod_type.text=protype
                        //  prod_name.text=name
                        //prod_city.text=cityName
                        item_quantity.number=qty
                        item_quantity.setOnValueChangeListener { view, oldValue, newValue ->
                            qty = newValue.toString()
                            ConstantObjects.usercart.get(position).advertisements.qty =
                                newValue.toString()
                            getTotalPrice()
                        }
                        prod_price.text = "$price ${getString(R.string.sar)}"
                        Picasso.get()
                            .load(ApiConstants.IMAGE_URL + image)
                            .into(prod_image)
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                ConstantObjects.usercart
            )
        }
    }
    private fun getTotalPrice() {
        var price = 0.0
        ConstantObjects.usercart.forEach {
            price += it.advertisements.price.toDouble() * it.advertisements.qty.toDouble()
        }
        price_total.text = price.toString()
    }

    fun GetUsersCartList(onSuccess: (() -> Unit)? = null) {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<AddToCartResponseModel> =
            malqa.GetUsersCartList(ConstantObjects.logged_userid)
        call.enqueue(object : retrofit2.Callback<AddToCartResponseModel?> {
            override fun onFailure(call: Call<AddToCartResponseModel?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<AddToCartResponseModel?>,
                response: Response<AddToCartResponseModel?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val resp: AddToCartResponseModel = response.body()!!
                        ConstantObjects.usercart =  resp.data
                        onSuccess?.invoke()
                    }
                }
                HelpFunctions.dismissProgressBar()

            }
        })


    }
}