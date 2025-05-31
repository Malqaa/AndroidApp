package com.malka.androidappp.botmnav_fragments.activities_main.business_signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
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
        if (!validateBFirstName() or !validateBLastName() or !validateBEmail() or !validateBEmail2()
            or !validateUserName() or !validateBName()
        ) {
            return
        } else {
            val Busifirstname = business_signup1_editText1.text.toString().trim()
            StaticBusinessRegistration.BfirstName = Busifirstname
            val BusilastName = business_signup1_editText2.text.toString().trim()
            StaticBusinessRegistration.BlastName = BusilastName
            val bEmail = business_signup1_editText3.text.toString().trim()
            StaticBusinessRegistration.BEmail1 = bEmail
            val bEmail2 = business_signup1_editText4.text.toString().trim()
            StaticBusinessRegistration.BEmail2 = bEmail2
            val Busername = business_signup1_editText5.text.toString().trim()
            StaticBusinessRegistration.Busername = Busername
            val busiName = business_signup1_editText6.text.toString().trim()
            StaticBusinessRegistration.businessName = busiName
            BsecondPg()
        }
    }

    private fun validateBFirstName(): Boolean {
        val BfirstName = business_signup1_editText1.text.toString().trim { it <= ' ' }
        return if (BfirstName.isEmpty()) {
            business_signup1_editText1.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            business_signup1_editText1.error = null
            true
        }
    }

    private fun validateBLastName(): Boolean {
        val BlastName = business_signup1_editText2.text.toString().trim { it <= ' ' }
        return if (BlastName.isEmpty()) {
            business_signup1_editText2.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            business_signup1_editText2.error = null
            true
        }
    }

    private fun validateBEmail(): Boolean {
        val BEmail = business_signup1_editText3.text.toString().trim { it <= ' ' }
        return if (BEmail.isEmpty()) {
            business_signup1_editText3.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(BEmail).matches()) {
            business_signup1_editText3.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            business_signup1_editText3.error = null
            true
        }
    }

    private fun validateBEmail2(): Boolean {
        val BEmail2 = business_signup1_editText4.text.toString().trim { it <= ' ' }
        return if (BEmail2.isEmpty()) {
            business_signup1_editText4.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(BEmail2).matches()) {
            business_signup1_editText4.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            business_signup1_editText4.error = null
            true
        }
    }

    private fun validateUserName(): Boolean {
        val userName = business_signup1_editText5.text.toString().trim { it <= ' ' }
        return if (userName.isEmpty()) {
            business_signup1_editText5.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (userName.length < 4) {
            business_signup1_editText5!!.error =
                getString(R.string.Usernamemusthaveatleast4characters)
            false
        } else {
            business_signup1_editText5.error = null
            true
        }
    }

    private fun validateBName(): Boolean {
        val BName = business_signup1_editText6.text.toString().trim { it <= ' ' }
        return if (BName.isEmpty()) {
            business_signup1_editText6.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (BName.length < 3) {
            business_signup1_editText6!!.error =
                getString(R.string.Businessnamemusthaveatleast3characters)
            false
        } else {
            business_signup1_editText6.error = null
            true
        }


    }
}