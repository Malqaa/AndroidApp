package com.malka.androidappp.fragments.shoppingcart5_location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.fragment_shopcart_location_info.*
import java.lang.Exception


class ShopcartLocationInfo : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopcart_location_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_cart_locationinfo.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_cart_locationinfo.setNavigationOnClickListener() { requireActivity().onBackPressed() }


        btn_cart_location.setOnClickListener() {
            if (Validate()) {
                if (SaveNewShippingAddress()) {
                    findNavController().navigate(R.id.locationinfo_paymentmethod)
                }
            }
        }
    }

    fun Validate(): Boolean {
        var RetVal: Boolean = false
        if (tx_location_countryy.text == null || tx_location_countryy.text.toString() == null || tx_location_countryy.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Country!",
                this@ShopcartLocationInfo.requireContext()
            );
            RetVal = false;
        } else if (tx_location_regionn.text == null || tx_location_regionn.text.toString() == null || tx_location_regionn.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Region!",
                this@ShopcartLocationInfo.requireContext()
            );
            RetVal = false;
        } else if (tx_location_addresss.text == null || tx_location_addresss.text.toString() == null || tx_location_addresss.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Address!",
                this@ShopcartLocationInfo.requireContext()
            );
            RetVal = false;
        } else if (tx_location_cityy.text == null || tx_location_cityy.text.toString() == null || tx_location_cityy.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter City!",
                this@ShopcartLocationInfo.requireContext()
            );
            RetVal = false;
        } else if (tx_location_mobilee.text == null || tx_location_mobilee.text.toString() == null || tx_location_mobilee.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Mobile!",
                this@ShopcartLocationInfo.requireContext()
            );
            RetVal = false;
        } else if (tx_location_firstnamee.text == null || tx_location_firstnamee.text.toString() == null || tx_location_firstnamee.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter First Name!",
                this@ShopcartLocationInfo.requireContext()
            );
            RetVal = false;
        } else if (tx_location_lastnamee.text == null || tx_location_lastnamee.text.toString() == null || tx_location_lastnamee.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Last Name!",
                this@ShopcartLocationInfo.requireContext()
            );
            RetVal = false;
        } else {
            RetVal = true;
        }
        return RetVal
    }

    fun SaveNewShippingAddress(): Boolean {
        var RetVal: Boolean = false
        try {
            var shippingaddress: ShippingAddressessData = ShippingAddressessData(
                id = "",
                country = tx_location_countryy.text.toString().trim(),
                region =  tx_location_regionn.text.toString().trim(),
                city =  tx_location_cityy.text.toString().trim(),
                address =  tx_location_addresss.text.toString().trim(),
                mobileNo =  tx_location_mobilee.text.toString().trim(),
                firstName =  tx_location_firstnamee.text.toString().trim(),
                lastName =  tx_location_lastnamee.text.toString().trim(),
                createdBy = "",
                createdOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
                updatedBy = "",
                updatedOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
                isActive = true,
                userId = ConstantObjects.logged_userid,
            )
            RetVal = HelpFunctions.AddNewShippingAddress(shippingaddress, this@ShopcartLocationInfo)
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
        return RetVal;
    }
}