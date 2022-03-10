package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.CreateAdvResponseBack
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.servicemodels.CreateAdvMainModel
import com.malka.androidappp.helper.Extension.clearPath
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListingFee : BaseActivity() {

    lateinit var categoryDataOrderSummary: TextView
    lateinit var listingFeeDataOrderSummary: TextView
    lateinit var featureDataOrderSummary: TextView
    lateinit var totalDataOrderSummary: TextView
    lateinit var finishButton: Button
    lateinit var radioButtonMaster: RadioButton
    lateinit var radioButtonVisa: RadioButton
    lateinit var editTextCVV: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_listing_fee)

        toolbar_title.text = getString(R.string.ConfirmDetails)
        back_btn.setOnClickListener {
            finish()
        }
        categoryDataOrderSummary = findViewById(R.id.categoryDataOrderSummary)
        listingFeeDataOrderSummary = findViewById(R.id.listingFeeDataOrderSummary)
        featureDataOrderSummary = findViewById(R.id.featureDataOrderSummary)
        totalDataOrderSummary = findViewById(R.id.totalDataOrderSummary)
        finishButton = findViewById(R.id.btn_finish)
        radioButtonMaster = findViewById(R.id.radio_btn_master)
        radioButtonVisa = findViewById(R.id.radio_btn_visa)
        editTextCVV = findViewById(R.id.editTextMasterCvv)



        setData()
        radioButtonValidation()

        finishButton.setOnClickListener() {
//            createListing()
//            createCarAdvApiCall()
            HelpFunctions.startProgressBar(this)
            mainModelToJSON()

        }
    }
    
   

    private fun radioButtonValidation() {
        radioButtonMaster.setOnCheckedChangeListener({ _, b ->
            if (b) {
                radioButtonVisa.isChecked = false
                editTextCVV.isEnabled = true
                editTextCVV.isFocusableInTouchMode = true
            }
        })
        radioButtonVisa.setOnCheckedChangeListener({ _, b ->
            if (b) {
                radioButtonMaster.isChecked = false
                editTextCVV.isEnabled = false
                editTextCVV.isFocusableInTouchMode = false
                editTextCVV.setText("")
            }
        })

    }

    private fun setData() {
        categoryDataOrderSummary.text = StaticClassAdCreate.mainCategory
        listingFeeDataOrderSummary.text = "SAR 100"
        featureDataOrderSummary.text = "iphone a1586 model"
        totalDataOrderSummary.text = "SAR NaN"

    }


  
 


    // Methods to handle dynamic ad creation
    private fun mainModelToJSON() {
        val mainModel = CreateAdvMainModel(
            Id = null,
            City = StaticClassAdCreate.city,
            Country = StaticClassAdCreate.country,
            name = StaticClassAdCreate.name,
            slug = StaticClassAdCreate.slug,
            Template = StaticClassAdCreate.template,
            Region = StaticClassAdCreate.region,
            Urgentexpirydate = StaticClassAdCreate.urgentexpirydate,
            Title = StaticClassAdCreate.title,
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
            isWatching = StaticClassAdCreate.iswatching,
            Isuserfavorite = false,
            listingType = StaticClassAdCreate.listingType,
            quantity = StaticClassAdCreate.quantity,
            featureexpirydate = StaticClassAdCreate.featureexpirydate,
            highlightexpirydate = StaticClassAdCreate.highlightexpirydate,
            phone = StaticClassAdCreate.phone,
            address = StaticClassAdCreate.address,
            pickupOption = StaticClassAdCreate.pickup_option,
            shippingOption = StaticClassAdCreate.shipping_option,
            pack4 = StaticClassAdCreate.pack4,
            description = StaticClassAdCreate.description,
            subtitle = StaticClassAdCreate.subtitle,
            producttitle = StaticClassAdCreate.producttitle,
            brand_new_item = StaticClassAdCreate.brand_new_item,
            enddate = StaticClassAdCreate.endtime,
            Images = StaticClassAdCreate.images,
            platform = "Android",
            iscashpaid = StaticClassAdCreate.iscashpaid,
            isbankpaid = StaticClassAdCreate.isbankpaid,
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
            Video = StaticClassAdCreate.video
        )

        // Model Class to JSON String
        val jsonString = Gson().toJson(mainModel)

        // JSON String HashMap
        var map: Map<String, String> = HashMap()
        map = Gson().fromJson(jsonString, map.javaClass)

        // Merging both HashMaps
        ConstantObjects.dynamic_json_dictionary.putAll(map)

        createAllAds(ConstantObjects.dynamic_json_dictionary)

    }

    private fun createAllAds(data: HashMap<String, String>) {

        val malqaa: MalqaApiService = RetrofitBuilder.createAd()

        val call: Call<CreateAdvResponseBack> = malqaa.createAllAd(data)
        call.enqueue(object : Callback<CreateAdvResponseBack> {

            override fun onResponse(
                call: Call<CreateAdvResponseBack>, response: Response<CreateAdvResponseBack>
            ) {
                if (response.isSuccessful) {
                    val importAdId = response.body()!!.data

                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(
                        importAdId + " " + getString(R.string.Youradhasbeencreatedsuccessfully),
                        this@ListingFee
                    )

//                    Toast.makeText(this@ListingFee, importAdId + "Ad has been created", Toast.LENGTH_LONG).show()


                 
                    
                    
                    startActivity(Intent(this@ListingFee, ContinueFragment::class.java).apply {
                      putExtra("AdvId",importAdId)
                      putExtra("Template",StaticClassAdCreate.template)
                      putExtra("sellerID",ConstantObjects.logged_userid)
                    })
                    clearPath()

                } else {
                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(getString(R.string.something_went_wrong), this@ListingFee)
//                    Toast.makeText(this@ListingFee, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<CreateAdvResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(t.message + " " + getString(R.string.failed), this@ListingFee)
//                Toast.makeText(this@ListingFee, t.message + "Failed", Toast.LENGTH_LONG).show()
            }
        })
    }
}