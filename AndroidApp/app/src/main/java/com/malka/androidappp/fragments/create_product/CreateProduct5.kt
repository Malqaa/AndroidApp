package com.malka.androidappp.fragments.create_product

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
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_create_product_pg5.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CreateProduct5 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_product_pg5, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_createpg5.setTitle("My Products")
        toolbar_createpg5.setTitleTextColor(Color.WHITE)
        toolbar_createpg5.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_createpg5.setNavigationOnClickListener({
            requireActivity().onBackPressed()
        })



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
            HelpFunctions.startProgressBar(requireActivity())
            checkExtraListingFees(view)
            CheckActivateAutolisting(view)
            createProductApiCall()
        }
    }


    fun createProductApiCall() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val resp = ModelCreateProduct(
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

        val call: Call<CreateProductResponseBack> = malqaa.createBusinessProduct(resp)
        call.enqueue(object : Callback<CreateProductResponseBack> {

            override fun onResponse(
                call: Call<CreateProductResponseBack>, response: Response<CreateProductResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data

                    Toast.makeText(
                        activity,
                        "Product has been created",
                        Toast.LENGTH_LONG
                    ).show()

                    HelpFunctions.dismissProgressBar()
                    val args = Bundle()
                    args.putString("AdvId", importAdId)
//                    args.putString("Template", StaticClassAdCreate.subcat)
                    NavHostFragment.findNavController(this@CreateProduct5)
                        .navigate(R.id.prom_continue_for_products, args)

                } else {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(activity, "Failed in Product Creation", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateProductResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                Toast.makeText(
                    activity,
                    t.message + "Failed in Product Creation",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


    }

    fun checkExtraListingFees(v: View) {
        if (checkBox1.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "0"
        } else if (checkBox2.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "0.55"
        } else if (checkBox3.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "0.65"
        } else if (checkBox4.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "3.45"
        } else if (checkBox4_5.isChecked) {
            StaticClassProductCreate.extrasListingOffers = "3.95"
        }
    }

    fun CheckActivateAutolisting(v: View) {

        if (active.isChecked) {
            StaticClassProductCreate.activateAutoListing = "active"
        } else {
            StaticClassProductCreate.activateAutoListing = "deactive"
        }
    }
}