package com.malka.androidappp.botmnav_fragments.activities_main.business_signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_business_signup_pg1.*

class BusinessSignupPg1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup_pg1)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        b_signuppg_1.setOnClickListener() {
            onBackPressed()
        }

        business_signup1_button.setOnClickListener() {
            confirminputBPg1()
        }
    }

    fun BsecondPg() {
        val intent = Intent(this@BusinessSignupPg1, BusinessSignupPg2::class.java)
        startActivity(intent)
        if (ConstantObjects.currentLanguage == "en") {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

    }

    fun confirminputBPg1() {
        if (! !validateBEmail() or !validateUserName()
        ) {
            return
        } else {

            StaticBusinessRegistration.BEmail1 = textEmaill.text.toString()
            StaticBusinessRegistration.Busername = userNamee.text.toString()
            BsecondPg()
        }
    }


    private fun validateBEmail(): Boolean {
        val BEmail = textEmaill.text.toString().trim { it <= ' ' }
        return if (BEmail.isEmpty()) {
            textEmaill.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(BEmail).matches()) {
            textEmaill.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            textEmaill.error = null
            true
        }
    }



    private fun validateUserName(): Boolean {
        val userName = userNamee.text.toString().trim { it <= ' ' }
        return if (userName.isEmpty()) {
            userNamee.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (userName.length < 4) {
            userNamee!!.error =
                getString(R.string.Usernamemusthaveatleast4characters)
            false
        } else {
            userNamee.error = null
            true
        }
    }


}