package com.malka.androidappp.design

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_seller_information.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_information)

        getSellerByID(ConstantObjects.logged_userid, ConstantObjects.logged_userid)

        toolbar_title.text = getString(R.string.merchant_page)
        back_btn.setOnClickListener {
            finish()
        }



        youtube_btn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/")
                )
            )
        })

        instagram_btn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/")
                )
            )
        })

        skype_btn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.skype.com/")
                )
            )
        })

        maps_btn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/")
                )
            )
        })

    }







    private fun getSellerByID(id: String, loggedUserID: String) {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<SellerResponseBack> = malqa.getAdSeller(id, loggedUserID)

        call.enqueue(object : Callback<SellerResponseBack> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<SellerResponseBack>,
                response: Response<SellerResponseBack>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: SellerResponseBack = response.body()!!
                        if (respone.status_code == 200) {

                            var sellerAdsList = response.body()!!.data.detailOfUser

                            sellerName.text = sellerAdsList.fullName ?: ""
                            seller_city.text = sellerAdsList.city
                            seller_number_tv.text = sellerAdsList.phone
                            member_sinceTv.text = "${getString(R.string.member_since)}:  ${sellerAdsList.member_since}"
                            member_ship.text = "${getString(R.string.membership_number)}: "


                            seller_information_recycler.adapter = GenericProductAdapter(
                                respone.data.advertisements,
                                this@SellerInformation
                            )


                        } else {

                            HelpFunctions.ShowLongToast(
                                getString(R.string.ErrorOccur),
                                this@SellerInformation
                            )
                        }
                    }

                } else {
                    HelpFunctions.dismissProgressBar()

                }
            }

            override fun onFailure(call: Call<SellerResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(t.message!!, this@SellerInformation)
            }
        })
    }
}