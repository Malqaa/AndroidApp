package com.malka.androidappp.activities_main.forgot.forgotpass_otpactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.malka.androidappp.BuildConfig
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.signup_account.signup_pg2.PostReqVerifyCode
import com.malka.androidappp.activities_main.forgot.forgot_changepass_reset_activity.ForgotChangepassActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.*
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.button4
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityForgotPassOtpcode : AppCompatActivity() {
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
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
        val userId: String? = intent.getStringExtra("userid")
        val otpcode: String = pinview234.getValue().toString().trim()
        val call: Call<PostReqVerifyCode> = malqa.verifycode(PostReqVerifyCode(userId!!, otpcode))
        call.enqueue(object : Callback<PostReqVerifyCode> {

            override fun onFailure(call: Call<PostReqVerifyCode>, t: Throwable) {

                t.message?.let { HelpFunctions.ShowLongToast(it, this@ActivityForgotPassOtpcode) }

            }

            override fun onResponse(
                call: Call<PostReqVerifyCode>,
                response: Response<PostReqVerifyCode>
            ) {

                if (response.isSuccessful) {
                    val getpin: String = pinview234.getValue().toString().trim()
                    val getidd = intent.getStringExtra("getid")
                    val intenddd = Intent(this@ActivityForgotPassOtpcode,ForgotChangepassActivity::class.java)
                    intenddd.putExtra("getidd",getidd)
                    intenddd.putExtra("getcodee",getpin)
                    startActivity(intenddd)
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationFailed),
                        this@ActivityForgotPassOtpcode
                    )
                }
            }
        })
    }
}