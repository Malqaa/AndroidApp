package com.malka.androidappp.activities_main.business_signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.BusinessAccountCreate
import com.malka.androidappp.design.Models.BusinessUserModel
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_switch_account.*
import kotlinx.android.synthetic.main.switch_account_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class Switch_Account : BaseActivity() {

    val creatAccountLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getBusinessUserList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_account)
        toolbar_title.text = getString(R.string.switch_accounts)
        back_btn.setOnClickListener {
            finish()
        }


        add_business_account_btn.setOnClickListener {
            val intent = Intent(this@Switch_Account, BusinessAccountCreate::class.java)
            creatAccountLauncher.launch(intent)
        }

        getBusinessUserList()
    }

    fun getBusinessUserList() {

        HelpFunctions.startProgressBar(this)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getBusinessUserList(ConstantObjects.logged_userid)



        call.enqueue(object : retrofit2.Callback<BusinessUserModel?> {
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
        accounts.text="${list.size+1} ${getString(R.string.accounts)}"
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