package com.malka.androidappp.design

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.network.service.insertAddressResponseBack
import kotlinx.android.synthetic.main.activity_add_address_main.*

class add_address_main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address_main)


        add_button.setOnClickListener {
            val  intent = Intent(this@add_address_main, add_address::class.java)
            startActivity(intent)

            insertAddress()
        }






//        select_country._setOnClickListener {
//            val items = List(3) { i ->
//                SearchableItem(1, "Dubai")
//                SearchableItem(2, "Abu Dhabi")
//                SearchableItem(3, "Sharjah")
//            }
//            select_region.showSpinner(items, "Select Region") {
//                select_country.text = it.title
//            }
//
//        }


//        select_region._setOnClickListener {
//            val items = List(3) { i ->
//                SearchableItem(1, "Dubai")
//                SearchableItem(2, "Abu Dhabi")
//                SearchableItem(3, "Sharjah")
//            }
//            select_region.showSpinner(items, "Select Region") {
//                select_region.text = it.title
//            }
//
//        }


//        select_city._setOnClickListener {
//            val items = List(3) { i ->
//                SearchableItem(1, "Al Bahah")
//                SearchableItem(2, "Al Aqiq")
//            }
//            select_region.showSpinner(items, "Select District") {
//                select_city.text = it.title
//            }
//
//        }

    }


    fun addressconfirmInput(v: View) {

        if (!!validateLastName() or !validateFirstName()  or !validatePhoneNumber()
        ) {
            return
        } else {


        }

    }

    private fun validateFirstName(): Boolean {
        val Inputname = firstname!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            firstname!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            firstname!!.error = null
            true
        }
    }

    private fun validateLastName(): Boolean {
        val Inputname = lastname!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            lastname!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            lastname!!.error = null
            true
        }
    }


    //phone no validation///
    private fun validatePhoneNumber(): Boolean {
        val numberInput =
            PhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            PhoneNumber!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            PhoneNumber!!.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else if (numberInput.length < 9) {
            PhoneNumber!!.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else {
            PhoneNumber!!.error = null
            true
        }
    }


    fun insertAddress() {

        HelpFunctions.startProgressBar(this)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

        val ftname = firstname.text.toString()
        val ltname = lastname.text.toString()
        val phonenumber = PhoneNumber.text.toString()
        val address = address.text.toString()
        val streetno = streetnumber.text.toString()

        val addAddress = GetAddressResponse.AddressModel(
            firstName = ftname,
            lastName = ltname,
            mobileNo = phonenumber,
            address = address,
            userId = "1234"

        )
        val call: retrofit2.Call<insertAddressResponseBack> = malqa.insertAddress(addAddress)

        call?.enqueue(object : retrofit2.Callback<insertAddressResponseBack?> {
            override fun onFailure(
                call: retrofit2.Call<insertAddressResponseBack?>?,
                t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@add_address_main, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: retrofit2.Call<insertAddressResponseBack?>,
                response: retrofit2.Response<insertAddressResponseBack?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: insertAddressResponseBack = response.body()!!
                        if (respone.status_code.equals("200")) {


                            Toast.makeText(
                                this@add_address_main,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

                            Toast.makeText(
                                this@add_address_main,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }


}