package com.malka.androidappp.botmnav_fragments.create_product

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_create_product_pg4.*

class CreateProduct4 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_product_pg4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_createpg4.setTitle("My Products")
        toolbar_createpg4.setTitleTextColor(Color.WHITE)
        toolbar_createpg4.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_createpg4.setNavigationOnClickListener({
            requireActivity().onBackPressed()
        })


        val shippingrates: Array<String> =
            arrayOf("Shipping rate is per item", "Shipping rate is per order")

        val pickUp: Array<String> =
            arrayOf("No pick-up", "Buyer can pick-up", "Buyer must pick-up")

        /////////////////For Shipping Rates Dropdown/Spinner/////////////////////
        val spinner: Spinner = requireActivity().findViewById(R.id.shippingrate_spinner)
        spinner.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            shippingrates
        )

        /////////////////For Pick up Dropdown/Spinner/////////////////////
        val spinner2: Spinner = requireActivity().findViewById(R.id.pickup_spinner)
        spinner2.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            pickUp
        )


        btn_createproduct4.setOnClickListener() {


            val shippingRateText: String = shippingrate_spinner.getSelectedItem().toString()
            StaticClassProductCreate.ShippingOptions = shippingRateText

            val pickUpText: String = pickup_spinner.getSelectedItem().toString()
            StaticClassProductCreate.PickUp = pickUpText

            checkShippingOptions(view)

            val lengthText: String = length_textt.getText().toString()
            StaticClassProductCreate.Length = lengthText

            val widthText: String = width_textt.getText().toString()
            StaticClassProductCreate.Width = widthText

            val heightText: String = height_textt.getText().toString()
            StaticClassProductCreate.Height = heightText

            val weightText: String = weight_textt.getText().toString()
            StaticClassProductCreate.Weight = weightText

            findNavController().navigate(R.id.product_page5)

        }
    }

    fun checkShippingOptions(v: View) {
        if (dont_know.isChecked) {
            StaticClassProductCreate.iDontknowTheShippingCostsYet = true
        } else if (free_shipping.isChecked) {
            StaticClassProductCreate.freeShippingWithinSaudia = true
        } else if (book_courier.isChecked) {
            StaticClassProductCreate.useBookACourierShippingCosts = true
        } else if (specify_shippingcost.isChecked) {
            StaticClassProductCreate.specifyShippingCosts = true
        }
    }
}