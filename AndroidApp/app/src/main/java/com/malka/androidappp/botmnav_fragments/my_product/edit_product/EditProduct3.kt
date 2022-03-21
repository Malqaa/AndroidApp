package com.malka.androidappp.botmnav_fragments.my_product.edit_product

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_product.ProductDetailModel
import com.malka.androidappp.botmnav_fragments.create_product.ProductResponseBack
import com.malka.androidappp.botmnav_fragments.create_product.StaticClassProductCreate
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_create_product_pg3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProduct3 : Fragment() {

    val offerDuration: Array<String> =
        arrayOf("1 day", "3 days", "7 days")

    val offerTo: Array<String> =
        arrayOf("Bidders/watchers", "Bidders only")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_createpg3.title = "Edit Product"
        toolbar_createpg3.setTitleTextColor(Color.WHITE)
        toolbar_createpg3.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_createpg3.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        /////////////////For Offer Duration Dropdown/Spinner/////////////////////
        val spinner: Spinner = requireActivity().findViewById(R.id.offerduartion_spinner)
        spinner.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            offerDuration
        )

        /////////////////For Offer To Dropdown/Spinner/////////////////////
        val spinner2: Spinner = requireActivity().findViewById(R.id.offerto_spinner)
        spinner2.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            offerTo
        )

        getProductbyidapi(StaticClassProductCreate.id, StaticClassProductCreate.userId)

        list_buynow.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                list_auction.isChecked = false
                start_price_textt.setEnabled(false)
                start_price_textt.setText("")
                start_price_textt.setInputType(InputType.TYPE_NULL)
                start_price_textt.setFocusableInTouchMode(false)

                specifyreserve_textt.setEnabled(false)
                specifyreserve_textt.setText("")
                specifyreserve_textt.setInputType(InputType.TYPE_NULL)
                specifyreserve_textt.setFocusableInTouchMode(false)
            } else {
                list_auction.isChecked = true
                start_price_textt.setEnabled(true)
                start_price_textt.setInputType(InputType.TYPE_CLASS_TEXT)
                start_price_textt.setFocusableInTouchMode(true)

                specifyreserve_textt.setEnabled(true)
                specifyreserve_textt.setInputType(InputType.TYPE_CLASS_TEXT)
                specifyreserve_textt.setFocusableInTouchMode(true)
            }
        })

        list_auction.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                list_buynow.isChecked = false
                start_price_textt.setEnabled(true)
                start_price_textt.setInputType(InputType.TYPE_CLASS_TEXT)
                start_price_textt.setFocusableInTouchMode(true)

                specifyreserve_textt.setEnabled(true)
                specifyreserve_textt.setInputType(InputType.TYPE_CLASS_TEXT)
                specifyreserve_textt.setFocusableInTouchMode(true)

            } else {
                list_buynow.isChecked = true

                start_price_textt.setEnabled(false)
                start_price_textt.setText("")
                start_price_textt.setInputType(InputType.TYPE_NULL)
                start_price_textt.setFocusableInTouchMode(false)

                specifyreserve_textt.setEnabled(false)
                specifyreserve_textt.setText("")
                specifyreserve_textt.setInputType(InputType.TYPE_NULL)
                specifyreserve_textt.setFocusableInTouchMode(false)
            }
        })

        btn_productinfo2.setOnClickListener() {

            radioButtonCheck(view)

            val buyNowText: String = buynow_textt.text.toString()
            StaticClassProductCreate.BuyNow = buyNowText

            StaticClassProductCreate.onlyAllowBidsFromAuthenticatedMembers = only_bids.isChecked

            StaticClassProductCreate.isfixedPriceOffer = fixedprice_details.isChecked

            val fixedPriceText: String = fixedprice_textt.text.toString()
            StaticClassProductCreate.fixedPriceOffer = fixedPriceText

            val offerDurationText: String = offerduartion_spinner.selectedItem.toString()
            StaticClassProductCreate.offerDuration = offerDurationText

            val offerTo: String = offerto_spinner.selectedItem.toString()
            StaticClassProductCreate.OfferTo = offerTo

            // Accepted Payment Methods
            StaticClassProductCreate.saudiBankDeposit = cash_sadeposit.isChecked
            StaticClassProductCreate.ping = bank_ping.isChecked
            StaticClassProductCreate.cash = bank_cash.isChecked
            StaticClassProductCreate.Paypal = bank_paypal.isChecked

            findNavController().navigate(R.id.edit_product3_to_4)
        }
    }

    fun radioButtonCheck(v: View) {
        if (list_buynow.isChecked) {
            StaticClassProductCreate.startPrice = ""
            StaticClassProductCreate.specifyReserve = ""
            StaticClassProductCreate.ListingType = "1"
        } else {
            StaticClassProductCreate.startPrice = start_price_textt.getText().toString()
            StaticClassProductCreate.specifyReserve = specifyreserve_textt.getText().toString()
            StaticClassProductCreate.ListingType = "2"
        }
    }

    fun getProductbyidapi(advid: String, loginUserId: String) {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ProductResponseBack> = malqa.getProductDetailById(advid, loginUserId)

        call.enqueue(object : Callback<ProductResponseBack> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<ProductResponseBack>,
                response: Response<ProductResponseBack>
            ) {

                if (response.isSuccessful) {
                    val details: ProductDetailModel = response.body()!!.data
                    if (details != null) {

                        only_bids.isChecked = details.onlyAllowBidsFromAuthenticatedMembers == true
                        fixedprice_details.isChecked = details.isfixedPriceOffer == true
                        fixedprice_textt.setText(details.fixedPriceOffer)
                        offerduartion_spinner.setSelection(offerDuration.indexOf(details.offerDuration))
                        offerto_spinner.setSelection(offerTo.indexOf(details.offerTo))
                        cash_sadeposit.isChecked = details.saudiBankDeposit == true
                        bank_ping.isChecked = details.ping == true
                        bank_cash.isChecked = details.cash == true
                        bank_paypal.isChecked = details.paypal == true
                        buynow_textt.setText(details.buyNowPrice)
                        start_price_textt.setText(details.startPrice)
                        specifyreserve_textt.setText(details.specifyReserve)
                        if (buynow_textt.text.toString().trim().isNotEmpty()) {
                            list_buynow.isChecked = true
                        } else {
                            list_auction.isChecked = true
                        }


                    } else {
                        HelpFunctions.ShowAlert(
                            this@EditProduct3.context, "Information", "No Record Found"
                        )
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@EditProduct3.context, "Information", "No Record Found"
                    )
                }
            }

            override fun onFailure(call: Call<ProductResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
