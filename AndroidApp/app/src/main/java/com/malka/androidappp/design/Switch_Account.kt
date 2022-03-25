package com.malka.androidappp.design

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.BusinessAccountCreate
import com.malka.androidappp.design.Models.BusinessUserModel
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_switch_account.*
import kotlinx.android.synthetic.main.switch_account_design.view.*

class Switch_Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_account)


        add_business_account_btn.setOnClickListener {
            val intentt = Intent(this@Switch_Account, BusinessAccountCreate::class.java)
            startActivity(intentt)
        }


    }

    override fun onResume() {
        super.onResume()

        getBusinessUserList()
    }



    fun getBusinessUserList() {

        HelpFunctions.startProgressBar(this)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getBusinessUserList(ConstantObjects.logged_userid)



        call?.enqueue(object : retrofit2.Callback<BusinessUserModel?> {
            override fun onFailure(call: retrofit2.Call<BusinessUserModel?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@Switch_Account, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: retrofit2.Call<BusinessUserModel?>, response: retrofit2.Response<BusinessUserModel?>) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: BusinessUserModel = response.body()!!
                        if (respone.status_code==200){

                            getBusinessUser(respone.data)

                            Toast.makeText(this@Switch_Account, respone.message , Toast.LENGTH_LONG).show()

                        }else{

                            Toast.makeText(this@Switch_Account, respone.message , Toast.LENGTH_LONG).show()
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }


    private fun getBusinessUser(list: List<BusinessUserModel.getBusinessList>) {
        business_rcv.adapter = object : GenericListAdapter<BusinessUserModel.getBusinessList>(
            R.layout.switch_account_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        business_name.text = businessName


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