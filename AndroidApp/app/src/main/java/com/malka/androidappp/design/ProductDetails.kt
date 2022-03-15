package com.malka.androidappp.design

import android.content.Intent
import com.malka.androidappp.activities_main.BaseActivity
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.activities_main.login.LoginClass
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.watchlist_fragment.WatchlistFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.Basicresponse
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.watchlist.watchlistadd
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.carspec_card1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetails : BaseActivity() {

    var advertisementID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        hideSystemUI(mainContainer)

        advertisementID = intent.getStringExtra(ConstantObjects.productDetails)?: ""

        back_button.setOnClickListener {
            finish()
        }
        add_Wish_btn.setOnClickListener {

            if (ConstantObjects.logged_userid.isEmpty()){

                startActivity(Intent(this, SignInActivity::class.java).apply {
                })


            }else{
                InsertAdToWatchlist()

            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java).apply {
        })
        finish()
    }







    fun InsertAdToWatchlist(): Boolean {
        var RetVal: Boolean = false
        try {
            val ad = watchlistadd(
                userid = ConstantObjects.logged_userid,
                advertisementId = advertisementID,
                remindertype = 0,


            )
            val malqa: MalqaApiService = RetrofitBuilder.InsertAdtoUserWatchlist()
            val call: Call<Basicresponse> = malqa.InsertAdtoUserWatchlist(ad)
            call.enqueue(object : Callback<Basicresponse> {
                override fun onResponse(
                    call: Call<Basicresponse>,
                    response: Response<Basicresponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var resp: Basicresponse = response.body()!!;
                            if (resp.status_code == 400 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                                RetVal = true
//                                HelpFunctions.GetUserWatchlist(context)
                                HelpFunctions.ShowLongToast(
                                    "Added Successfully",
                                    this@ProductDetails
                                );
                            }
                        } else {
                            HelpFunctions.ShowLongToast(
                                "Error During Addition",
                                this@ProductDetails
                            );
                        }
                    } else {
                        HelpFunctions.ShowLongToast(
                            "Error During Addition",
                            this@ProductDetails
                        );
                    }
                }

                override fun onFailure(
                    call: Call<Basicresponse>,
                    t: Throwable
                ) {
                    HelpFunctions.ShowLongToast(
                        "Error During Addition",
                        this@ProductDetails
                    );
                }
            })
        } catch (ex: Exception) {
            throw ex
        }
        return RetVal
    }


    fun AdAlreadyAddedToWatchList(adreferenceId: String): Boolean {
        var RetVal: Boolean = false;
        try {
            if (adreferenceId != null && adreferenceId.trim().length > 0) {
                if (ConstantObjects.userwatchlist != null && ConstantObjects.userwatchlist!!.data != null && ConstantObjects.userwatchlist!!.data.size > 0) {
                    for (IndWatch in ConstantObjects.userwatchlist!!.data) {
                        if (IndWatch.advertisement != null) {
                            if (IndWatch.advertisement.referenceId.trim().toUpperCase()
                                    .equals(adreferenceId.trim().toUpperCase())
                            ) {
                                RetVal = true
                                break
                            } else {
                                RetVal = false
                                continue
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            throw  ex
        }
        return RetVal
    }

}