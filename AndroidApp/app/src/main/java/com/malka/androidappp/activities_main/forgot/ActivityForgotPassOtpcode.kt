package com.malka.androidappp.activities_main.forgot

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.malka.androidappp.BuildConfig
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.domain.models.servicemodels.PostReqVerifyCode
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.BasicResponse
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.*
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.button4
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityForgotPassOtpcode : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_otpcode)


        val getcodee:String? = intent.getStringExtra("getcode")
        if(BuildConfig.DEBUG){
            pinview234.setValue(getcodee!!)
        }




        button4.setOnClickListener {
            changepassotpconfirmInput()
        }
    }


    private fun validatePinn(): Boolean {

        val inputtt = pinview234.getValue()
        return if (inputtt.length != 4) {
            redmessage.setText("Field can't be empty")
            redmessage.setVisibility(View.VISIBLE)
            false
        } else {
            redmessage.error = null
            redmessage.setVisibility(View.GONE)
            true
        }
    }

    fun changepassotpconfirmInput() {
        if (!validatePinn()) {
            return
        } else {
            verifyotpcode()


        }

    }

    //////////////////////////////////////Api Post Verify//////////////////////////////////////////////////
    fun verifyotpcode() {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val userId: String? = intent.getStringExtra("getid")
        val otpcode: String = pinview234.getValue().toString().trim()
        val call= malqa.verifycode(PostReqVerifyCode(userId=userId.toString(),code= otpcode))
        call.enqueue(object : Callback<BasicResponse> {

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                HelpFunctions.dismissProgressBar()

                t.message?.let {
                    HelpFunctions.ShowLongToast(it, this@ActivityForgotPassOtpcode) }

            }

            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {

                if (response.isSuccessful) {
                    val data=response.body()
                    if (data!!.status_code == 200) {
                        val getpin: String = pinview234.getValue().toString().trim()
                        val getidd = intent.getStringExtra("getid")
                        val intenddd = Intent(this@ActivityForgotPassOtpcode, ForgotChangepassActivity::class.java)
                        intenddd.putExtra("getidd",getidd)
                        intenddd.putExtra("getcodee",getpin)
                        startActivity(intenddd)
                        finish()
                    } else {
                        showError(data.message)
                    }


                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationFailed),
                        this@ActivityForgotPassOtpcode
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })
    }
}