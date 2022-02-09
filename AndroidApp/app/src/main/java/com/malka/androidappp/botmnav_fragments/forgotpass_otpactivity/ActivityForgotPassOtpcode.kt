package com.malka.androidappp.botmnav_fragments.forgotpass_otpactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.forgot_changepass_reset_activity.ForgotChangepassActivity
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.*
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.button4

class ActivityForgotPassOtpcode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_otpcode)


        val getcodee:String? = intent.getStringExtra("getcode")
        pinview234.setValue(getcodee!!)




        button4.setOnClickListener(){
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

            val getpin: String = pinview234.getValue().toString().trim()
            val getidd = intent.getStringExtra("getid")
            val intenddd = Intent(this@ActivityForgotPassOtpcode,ForgotChangepassActivity::class.java)
            intenddd.putExtra("getidd",getidd)
            intenddd.putExtra("getcodee",getpin)
            startActivity(intenddd)

        }

    }
}