package com.malka.androidappp.botmnav_fragments.my_product.edit_product

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_product.ProductDetailModel
import com.malka.androidappp.botmnav_fragments.create_product.ProductResponseBack
import com.malka.androidappp.botmnav_fragments.create_product.StaticClassProductCreate
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_create_product_pg4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProduct4 : Fragment() {

    val shippingrates: Array<String> =
        arrayOf("Shipping rate is per item", "Shipping rate is per order")

    val pickUp: Array<String> =
        arrayOf("No pick-up", "Buyer can pick-up", "Buyer must pick-up")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_createpg4.title = "Edit Product"
        toolbar_createpg4.setTitleTextColor(Color.WHITE)
        toolbar_createpg4.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_createpg4.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


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
        getProductbyidapi(StaticClassProductCreate.id,StaticClassProductCreate.userId)

        btn_createproduct4.setOnClickListener() {


            val shippingRateText: String = shippingrate_spinner.selectedItem.toString()
            StaticClassProductCreate.ShippingOptions = shippingRateText

            val pickUpText: String = pickup_spinner.selectedItem.toString()
            StaticClassProductCreate.PickUp = pickUpText

            checkShippingOptions(view)

            val lengthText: String = length_textt.text.toString()
            StaticClassProductCreate.Length = lengthText

            val widthText: String = width_textt.text.toString()
            StaticClassProductCreate.Width = widthText

            val heightText: String = height_textt.text.toString()
            StaticClassProductCreate.Height = heightText

            val weightText: String = weight_textt.text.toString()
            StaticClassProductCreate.Weight = weightText

            findNavController().navigate(R.id.edit_product4_to_5)

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

                        shippingrate_spinner.setSelection(shippingrates.indexOf(details.shippingOptions))
                        pickup_spinner.setSelection(pickUp.indexOf(details.pickUp))
                        dont_know.isChecked = details.iDontknowTheShippingCostsYet == true
                        free_shipping.isChecked = details.freeShippingWithinSaudia ==  true
                        book_courier.isChecked = details.useBookACourierShippingCosts == true
                        specify_shippingcost.isChecked = details.specifyShippingCosts == true
                        length_textt.setText(details.length)
                        width_textt.setText(details.width)
                        height_textt.setText(details.height)
                        weight_textt.setText(details.weight)

                    } else {
                        HelpFunctions.ShowAlert(
                            this@EditProduct4.context, "Information", "No Record Found"
                        )
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@EditProduct4.context, "Information", "No Record Found"
                    )
                }
            }

            override fun onFailure(call: Call<ProductResponseBack>, t: Throwable) {
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}

