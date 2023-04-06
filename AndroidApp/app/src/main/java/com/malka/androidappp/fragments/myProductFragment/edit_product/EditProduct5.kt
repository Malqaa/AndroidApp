package com.malka.androidappp.fragments.myProductFragment.edit_product

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.malka.androidappp.R
import com.malka.androidappp.fragments.create_product.*
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_create_product_pg5.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class EditProduct5 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_createpg5.title = "Edit Product"
        toolbar_createpg5.setTitleTextColor(Color.WHITE)
        toolbar_createpg5.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_createpg5.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        getProductbyidapi(StaticClassProductCreate.id, StaticClassProductCreate.userId)
        checkBox1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox2.isChecked = false
                checkBox3.isChecked = false
                checkBox4.isChecked = false
                checkBox4_5.isChecked = false
            }
        })

        checkBox2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox1.isChecked = false
                checkBox3.isChecked = false
                checkBox4.isChecked = false
                checkBox4_5.isChecked = false
            }
        })

        checkBox3.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox2.isChecked = false
                checkBox1.isChecked = false
                checkBox4.isChecked = false
                checkBox4_5.isChecked = false
            }
        })

        checkBox4.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox2.isChecked = false
                checkBox3.isChecked = false
                checkBox1.isChecked = false
                checkBox4_5.isChecked = false
            }
        })

        checkBox4_5.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox2.isChecked = false
                checkBox3.isChecked = false
                checkBox4.isChecked = false
                checkBox1.isChecked = false
            }
        })

        btn_createproduct5.setOnClickListener() {
            checkExtraListingFees(view)
            CheckActivateAutolisting(view)
            editProductApiCall()
        }
    }


    fun editProductApiCall() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val resp = ModelEditProduct(
            id = StaticClassProductCreate.id,
            userId = StaticClassProductCreate.userId,
            code = StaticClassProductCreate.code,
            sKU = StaticClassProductCreate.sKU,
            title = StaticClassProductCreate.title,
            subTitle = StaticClassProductCreate.subTitle,
            brand = StaticClassProductCreate.brand,
            barcodeGTIN = StaticClassProductCreate.barcodeGTIN,
            images = StaticClassProductCreate.images,
            manufacturersCode = StaticClassProductCreate.manufacturersCode,
            stock = StaticClassProductCreate.stock,
            listingDuration = StaticClassProductCreate.listingDuration,
            specifyEndTime = StaticClassProductCreate.specifyEndTime,
            description = StaticClassProductCreate.description,
            unwantedChristmasgift = StaticClassProductCreate.unwantedChristmasgift,
            color = StaticClassProductCreate.color,
            size = StaticClassProductCreate.size,
            ddlbrand = StaticClassProductCreate.ddlbrand,
            BuyNow = StaticClassProductCreate.BuyNow,
            startPrice = StaticClassProductCreate.startPrice,
            specifyReserve = StaticClassProductCreate.specifyReserve,
            fixedPriceOffer = StaticClassProductCreate.fixedPriceOffer,
            offerDuration = StaticClassProductCreate.offerDuration,
            OfferTo = StaticClassProductCreate.OfferTo,
            Length = StaticClassProductCreate.Length,
            Width = StaticClassProductCreate.Width,
            Height = StaticClassProductCreate.Height,
            Weight = StaticClassProductCreate.Weight,
            extrasListingOffers = StaticClassProductCreate.extrasListingOffers,
            activateAutoListing = StaticClassProductCreate.activateAutoListing,
            BuyNowAuction = StaticClassProductCreate.BuyNowAuction,
            ShippingOptions = StaticClassProductCreate.ShippingOptions,
            PickUp = StaticClassProductCreate.PickUp,
            onlyAllowBidsFromAuthenticatedMembers = StaticClassProductCreate.onlyAllowBidsFromAuthenticatedMembers,
            isfixedPriceOffer = StaticClassProductCreate.isfixedPriceOffer,
            saudiBankDeposit = StaticClassProductCreate.saudiBankDeposit,
            ping = StaticClassProductCreate.ping,
            cash = StaticClassProductCreate.cash,
            iDontknowTheShippingCostsYet = StaticClassProductCreate.iDontknowTheShippingCostsYet,
            freeShippingWithinSaudia = StaticClassProductCreate.freeShippingWithinSaudia,
            useBookACourierShippingCosts = StaticClassProductCreate.useBookACourierShippingCosts,
            specifyShippingCosts = StaticClassProductCreate.specifyShippingCosts,
            useShippingTemplate = StaticClassProductCreate.useShippingTemplate,
            UnlimitedStock = StaticClassProductCreate.UnlimitedStock,
            Paypal = StaticClassProductCreate.Paypal,
            ListingType = StaticClassProductCreate.ListingType,
            CreatedDate = Calendar.getInstance().time,
            updatedon = StaticClassProductCreate.updatedon,
            updateby = StaticClassProductCreate.updateby,
            City = StaticClassProductCreate.City,
            Country = StaticClassProductCreate.Country,
            Slug = StaticClassProductCreate.Slug,
            Template = StaticClassProductCreate.Template,
            Region = StaticClassProductCreate.Region,
        )

        val call: Call<EditProductResponseBack> = malqaa.editBusinessProduct(resp)
        call.enqueue(object : Callback<EditProductResponseBack> {

            override fun onResponse(
                call: Call<EditProductResponseBack>, response: Response<EditProductResponseBack>
            ) {
                if (response.isSuccessful) {

                    NavHostFragment.findNavController(this@EditProduct5)
                        .navigate(R.id.edit_product5_to_continue)

                } else {
                    Toast.makeText(activity, "Failed to Edit Product", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<EditProductResponseBack>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.message + "Failed to Edit Product",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun checkExtraListingFees(v: View) {
        if (checkBox1.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "Basic"
        } else if (checkBox2.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "Gallery"
        } else if (checkBox3.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "Gallery Plus"
        } else if (checkBox4.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "Feature"
        } else if (checkBox4_5.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "Feature Combo"
        }
    }

    fun CheckActivateAutolisting(v: View) {

        if (active.isChecked) {
            StaticClassProductCreate.activateAutoListing = "Yes"
        } else {
            StaticClassProductCreate.activateAutoListing = "No"
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

                        if (details.extrasListingOffers!!.matches("Basic".toRegex())) {
                            checkBox1.isChecked = true
                        } else if (details.extrasListingOffers.matches("Gallery".toRegex())) {
                            checkBox2.isChecked = true
                        } else if (details.extrasListingOffers.matches("Gallery Plus".toRegex())) {
                            checkBox3.isChecked = true
                        } else if (details.extrasListingOffers.matches("Feature".toRegex())) {
                            checkBox4.isChecked = true
                        } else if (details.extrasListingOffers.matches("Feature Combo".toRegex())) {
                            checkBox4_5.isChecked = true
                        }
//                        if (details.activateAutoListing!!.matches("Yes".toRegex()) || details.activateAutoListing.matches(
//                                "active".toRegex())
//                        ) {
//                            active.isChecked = true
//                            de_actiive.isChecked = false
//                        } else {
//                            active.isChecked = false
//                            de_actiive.isChecked = true
//                        }


                    } else {
                        HelpFunctions.ShowAlert(
                            this@EditProduct5.context, "Information", "No Record Found"
                        )
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@EditProduct5.context, "Information", "No Record Found"
                    )
                }
            }

            override fun onFailure(call: Call<ProductResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}

