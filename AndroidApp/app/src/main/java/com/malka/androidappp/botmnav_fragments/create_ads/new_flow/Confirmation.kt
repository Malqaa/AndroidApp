package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_confirmation.*
import kotlinx.android.synthetic.main.toolbar_main.*

class Confirmation : BaseActivity() {

    lateinit var categoryData: TextView
    lateinit var titleData: TextView
    lateinit var subTitleData: TextView
    lateinit var quantityData: TextView
    lateinit var regionData: TextView
    lateinit var cityData: TextView
    lateinit var buyNowData: TextView
    lateinit var acceptedPaymentData: TextView
    lateinit var pickUpData: TextView
    lateinit var durationShippingData: TextView
    lateinit var timingData: TextView
    lateinit var shippingData: TextView
    lateinit var packageData: TextView
    lateinit var subtitleFeeData: TextView
    lateinit var listingFeeData: TextView
    lateinit var featureFeeData: TextView
    lateinit var selectedImages: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_confirmation)

        toolbar_title.text = getString(R.string.ConfirmDetails)
        back_btn.setOnClickListener {
            finish()
        }
        categoryData = findViewById(R.id.categoryData)
        titleData = findViewById(R.id.TitleData)
        subTitleData = findViewById(R.id.subTitleData)
        quantityData = findViewById(R.id.quantityData)
        regionData = findViewById(R.id.RegionData)
        cityData = findViewById(R.id.cityData)
        buyNowData = findViewById(R.id.buyNowData)
        acceptedPaymentData = findViewById(R.id.acceptedPaymentData)
        pickUpData = findViewById(R.id.PickupOptionData)
        durationShippingData = findViewById(R.id.DurationShippingData)
        timingData = findViewById(R.id.timingData)
        shippingData = findViewById(R.id.shippingData)
        packageData = findViewById(R.id.PackageNameData)
        subtitleFeeData = findViewById(R.id.subtitleFeeData)
        listingFeeData = findViewById(R.id.listingFeeData)
        featureFeeData = findViewById(R.id.featureFeeData)
        selectedImages = findViewById(R.id.selectedImages)

        setData()


        btn_confirm_details.setOnClickListener() {
            startActivity(Intent(this, ListingFee::class.java).apply {
            })
        }

    }


    fun setData() {
        categoryData.text = StaticClassAdCreate.mainCategory
        titleData.text = StaticClassAdCreate.producttitle
        subTitleData.text = StaticClassAdCreate.subtitle
        quantityData.text = StaticClassAdCreate.quantity
        regionData.text = StaticClassAdCreate.region
        cityData.text = StaticClassAdCreate.city
        buyNowData.text = StaticClassAdCreate.price
        acceptedPaymentData.text =
            StaticClassAdCreate.iscashpaid + " " + StaticClassAdCreate.isbankpaid
        pickUpData.text = StaticClassAdCreate.pickup_option
        durationShippingData.text = StaticClassAdCreate.duration
        timingData.text = StaticClassAdCreate.timepicker
        shippingData.text = StaticClassAdCreate.shipping_option
        packageData.text = "No Pick up"
        subtitleFeeData.text = "12"
        listingFeeData.text = "123"
        featureFeeData.text = "56"

//        selectedImages = findViewById(R.id.selectedImages)

    }
}