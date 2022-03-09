package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Filter
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.hideLoader
import com.malka.androidappp.helper.showLoader
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.network.service.insertAddressResponseBack
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.add_address_design.view.*

class add_address : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_add_address)

        getAddress()


        add_new_add.setOnClickListener {

            val  intent = Intent(this@add_address, add_address_main::class.java)
            startActivity(intent)

        }


    }




    fun getAddress() {

        loader.showLoader()

        val malqa: MalqaApiService = RetrofitBuilder.createAccountsInstance()
        val call = malqa.getAddress("1234")



        call?.enqueue(object : retrofit2.Callback<GetAddressResponse?> {
            override fun onFailure(call: retrofit2.Call<GetAddressResponse?>?, t: Throwable) {
                loader.hideLoader()

                Toast.makeText(this@add_address, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: retrofit2.Call<GetAddressResponse?>, response: retrofit2.Response<GetAddressResponse?>) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: GetAddressResponse = response.body()!!
                        if (respone.status_code==200){

                            AdressAdaptor(respone.data)

                            Toast.makeText(this@add_address, respone.message ,Toast.LENGTH_LONG).show()

                        }else{

                            Toast.makeText(this@add_address, respone.message ,Toast.LENGTH_LONG).show()
                        }
                    }

                }
                loader.hideLoader()
            }
        })



    }


    private fun AdressAdaptor(list: List<GetAddressResponse.AddressModel>) {
        category_rcv.adapter = object : GenericListAdapter<GetAddressResponse.AddressModel>(
            R.layout.add_address_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        lastname.text=lastName
                        first_name.text=firstName
                        phonenum.text=mobileNo
                        adress.text=address

                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }
}