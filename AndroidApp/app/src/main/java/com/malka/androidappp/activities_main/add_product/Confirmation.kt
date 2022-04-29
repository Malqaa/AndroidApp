package com.malka.androidappp.activities_main.add_product

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.*
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.CreateAdvMainModel
import com.malka.androidappp.servicemodels.CreateAdvResponseBack
import com.malka.androidappp.servicemodels.Selection
import kotlinx.android.synthetic.main.fragment_confirmation.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Confirmation : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_confirmation)

        toolbar_title.text = getString(R.string.distinguish_your_product)
        back_btn.setOnClickListener {
            finish()
        }

        setData()


        btn_confirm_details.setOnClickListener {
            val paymentMethodList: ArrayList<Selection> = ArrayList()
            paymentMethodList.apply {
                clear()
                add(Selection(getString(R.string.Saudiabankdeposit)))
                add(Selection(getString(R.string.visa_mastercard)))
            }
            CommonBottomSheet().showPaymentOption(getString(R.string.PaymentOptions),paymentMethodList,this) {
                when (it.name) {
                    getString(R.string.saudi_bank_deposit) -> {

                    }
                    getString(R.string.visa_mastercard) -> {
                        CommonAPI().GetUserCreditCards(this) {
                            CommonBottomSheet().showCardSelection(this, it, {
                                mainModelToJSON()
                            }) {
                                btn_confirm_details.performClick()
                            }
                        }

                    }
                }


            }

        }

    }


    // Methods to handle dynamic ad creation
    private fun mainModelToJSON() {
        HelpFunctions.startProgressBar(this)


//        if (StaticClassAdCreate.brand_new_item.equals(getString(R.string.New))) {
//            StaticClassAdCreate.brand_new_item = "on"
//        } else if (StaticClassAdCreate.brand_new_item.equals(getString(R.string.used))) {
//            StaticClassAdCreate.brand_new_item = "Off"
//
//        }


        val mainModel = CreateAdvMainModel(
            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            name = StaticClassAdCreate.name,
            slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.template,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            title = StaticClassAdCreate.title,
            Price = StaticClassAdCreate.price,
            user = ConstantObjects.logged_userid,
            StartingPrice = StaticClassAdCreate.startingPrice,
            ReservePrice = StaticClassAdCreate.reservedPrice,

            Duration = StaticClassAdCreate.duration,
            EndTime = StaticClassAdCreate.endtime,
            FixLength = StaticClassAdCreate.fixLength,
            Timepicker = StaticClassAdCreate.timepicker,
            DateTime = StaticClassAdCreate.endtime,

            isActive = false,
            isWatching = false,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity.toInt(),
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.item_description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            enddate = StaticClassAdCreate.endtime,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isvisapaid = StaticClassAdCreate.isvisapaid,
            isbankpaid = StaticClassAdCreate.isbankpaid,
            isnegotiable = StaticClassAdCreate.isnegotiable,
            subcatone = StaticClassAdCreate.subcatone,
            subcattwo = StaticClassAdCreate.subcattwo,
            subcatthree = StaticClassAdCreate.subcatthree,
            subcatfour = StaticClassAdCreate.subcatfour,
            subcatfive = StaticClassAdCreate.subcatfive,
            subcatsix = StaticClassAdCreate.subcatsix,
            subcatonekey = StaticClassAdCreate.subcatonekey,
            subcattwokey = StaticClassAdCreate.subcattwokey,
            subcatthreekey = StaticClassAdCreate.subcatthreekey,
            subcatfourkey = StaticClassAdCreate.subcatfourkey,
            subcatfivekey = StaticClassAdCreate.subcatfivekey,
            subcatsixkey = StaticClassAdCreate.subcatsixkey,
            category = StaticClassAdCreate.subCategoryPath[0],
            Video = StaticClassAdCreate.video,
            brand_new_item = StaticClassAdCreate.brand_new_item,
        )


        val imageList: ArrayList<String> = ArrayList()

        StaticClassAdCreate.images.forEach {
            if (it.is_main == true) {
                imageList.add(it.base64)
            }
        }

        StaticClassAdCreate.images.forEach {
            if (it.is_main != true) {
                imageList.add(it.base64)
            }
        }

//        imageList.clear()
//        imageList.add("data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUA" +
//                "    AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO" +
//                "        9TXL0Y4OHwAAAABJRU5ErkJggg==")

        mainModel.images = imageList

        val jsonString = Gson().toJson(mainModel)

        var map: Map<String, String> = HashMap()
        map = Gson().fromJson(jsonString, map.javaClass)

        ConstantObjects.dynamic_json_dictionary.putAll(map)
        val testing = Gson().toJson(map)
        print(testing)

        createAllAds(ConstantObjects.dynamic_json_dictionary)

    }

    private fun createAllAds(data: HashMap<String, String>) {

        val malqaa = RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<CreateAdvResponseBack> = malqaa.createAllAd(data)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val AdvId = response.body()!!.data
                    HelpFunctions.dismissProgressBar()
                    startActivity(Intent(this@Confirmation, SuccessProduct::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        putExtra("AdvId", AdvId)
                        putExtra("Template", StaticClassAdCreate.template)
                        putExtra("sellerID", ConstantObjects.logged_userid)
                    })
                    finish()

                    StaticClassAdCreate.subCategoryPath.clear()
                    Extension.clearPath()

                } else {
                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(
                        getString(R.string.something_went_wrong),
                        this@Confirmation
                    )
                }

            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(
                    t.message + " " + getString(R.string.failed),
                    this@Confirmation
                )
            }
        })
    }

    fun setData() {
        StaticClassAdCreate.images.filter {
            it.is_main == true
        }.let {
            if (it.size > 0) {
                selectedImages.setImageURI(it.get(0).uri)
            }
        }

        if (StaticClassAdCreate.selectPromotiion == null) {
            calculation(0)
        } else {
            val package_cost = StaticClassAdCreate.selectPromotiion!!.packageprice.toInt()
            calculation(package_cost)
        }





        PickupOptionData.text = StaticClassAdCreate.shipping_option


        product_type.text = getCategortList()
        item_condition.text = StaticClassAdCreate.brand_new_item
        TitleData.text = StaticClassAdCreate.producttitle
        product_detail.text = StaticClassAdCreate.item_description
        subTitleData.text = StaticClassAdCreate.subtitle
        quantityData.text = StaticClassAdCreate.quantity


        when (StaticClassAdCreate.listingType) {
            "1" -> {
                fixed_price.show()
                purchasing_price_.show()
                purchasing_price_tv.text = StaticClassAdCreate.price
            }
            "2" -> {
                Auction.show()
                auction_start_price.show()
                minimum_price.show()
                auction_start_price_tv.text = StaticClassAdCreate.reservedPrice
                minimum_price_tv.text = StaticClassAdCreate.startingPrice

            }
            "3" -> {
                Auction.show()
                purchasing_price_.show()
                auction_start_price.show()
                minimum_price.show()
                purchasing_price_tv.text = StaticClassAdCreate.price
                auction_start_price_tv.text = StaticClassAdCreate.reservedPrice
                minimum_price_tv.text = StaticClassAdCreate.startingPrice
            }
        }

        if (!StaticClassAdCreate.isbankpaid) {
            saudi_bank_deposit.show()
        }
        if (!StaticClassAdCreate.isvisapaid) {
            Visa.show()
        }


        timingData.text = "${StaticClassAdCreate.endtime} ${StaticClassAdCreate.timepicker}"

        if (StaticClassAdCreate.selectPromotiion == null) {
            select_package_layout.hide()

        } else {
            package_name_tv.text = StaticClassAdCreate.selectPromotiion!!.packagename
            package_price_tv.text =
                "${StaticClassAdCreate.selectPromotiion!!.packageprice} ${getString(R.string.rial)}"
        }


    }

    private fun calculation(package_cost: Int) {
        val TaxAmount = package_cost * 12 / 100
        val total = package_cost + TaxAmount
        package_cost_tv.text = "${package_cost} ${getString(R.string.rial)}"
        added_tax.text = "${TaxAmount} ${getString(R.string.rial)}"
        total_tv.text = "${total} ${getString(R.string.rial)}"
        discount_tv.text = "${"0"} ${getString(R.string.rial)}"
    }
}