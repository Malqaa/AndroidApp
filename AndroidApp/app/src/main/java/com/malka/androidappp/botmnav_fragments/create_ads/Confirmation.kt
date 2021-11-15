package com.malka.androidappp.botmnav_fragments.create_ads

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_confirmation.*
import kotlinx.android.synthetic.main.fragment_list_details.*
import kotlinx.android.synthetic.main.fragment_list_details.toolbar_listdetails

class Confirmation : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        categoryData = requireActivity().findViewById(R.id.categoryData)
        titleData = requireActivity().findViewById(R.id.TitleData)
        subTitleData = requireActivity().findViewById(R.id.subTitleData)
        quantityData = requireActivity().findViewById(R.id.quantityData)
        regionData = requireActivity().findViewById(R.id.RegionData)
        cityData = requireActivity().findViewById(R.id.cityData)
        buyNowData = requireActivity().findViewById(R.id.buyNowData)
        acceptedPaymentData = requireActivity().findViewById(R.id.acceptedPaymentData)
        pickUpData = requireActivity().findViewById(R.id.PickupOptionData)
        durationShippingData = requireActivity().findViewById(R.id.DurationShippingData)
        timingData = requireActivity().findViewById(R.id.timingData)
        shippingData = requireActivity().findViewById(R.id.shippingData)
        packageData = requireActivity().findViewById(R.id.PackageNameData)
        subtitleFeeData = requireActivity().findViewById(R.id.subtitleFeeData)
        listingFeeData = requireActivity().findViewById(R.id.listingFeeData)
        featureFeeData = requireActivity().findViewById(R.id.featureFeeData)
        selectedImages = requireActivity().findViewById(R.id.selectedImages)

        setData()

        ////////////////////////////////////////////////////////
        toolbar_confirmation.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_confirmation.title = getString(R.string.ConfirmDetails)
        toolbar_confirmation.setTitleTextColor(Color.WHITE)
        toolbar_confirmation.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_confirmation.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }

        btn_confirm_details.setOnClickListener() {
            findNavController().navigate(R.id.confirmation_to_listing_fee)

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

//        selectedImages = requireActivity().findViewById(R.id.selectedImages)

    }
}