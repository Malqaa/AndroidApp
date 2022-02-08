package com.malka.androidappp.botmnav_fragments.activities_main.business_signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_business_signup_pg1.*
import kotlinx.android.synthetic.main.activity_business_signup_pg2.*
import kotlinx.android.synthetic.main.activity_signup_pg1.*

class BusinessSignupPg2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup_pg2)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        busi_signup2_btn.setOnClickListener() {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        business_signup2_button.setOnClickListener() {
            confirminputtBpg2()
        }
        ic_youtube_select.setOnClickListener() {
           // ic_youtube_select.isSelected=!ic_youtube_select.isSelected
            if(ic_youtube_select.isSelected){
                ic_youtube.show()
            }else{
                ic_youtube.hide()
            }
        }
        ic_twitter_select.setOnClickListener() {
            ic_twitter_select.isSelected=!ic_twitter_select.isSelected
            if(ic_twitter_select.isSelected){
                ic_twitter.show()
            }else{
                ic_twitter.hide()
            }
        }
    }

    fun BthirdPg() {
        val intent = Intent(this@BusinessSignupPg2, BusinessSignupPg3::class.java)
        startActivity(intent)
        if (ConstantObjects.currentLanguage == "en") {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    fun confirminputtBpg2() {
        if (!validateBActualAddress() or !validateBBillingAddress() or !validateBphoneNum()
            or !validateBpass() or !validateBSignupConfrmPassword()
        ) {
            return
        } else {
            val Bactualadress = busi_signup2_edittext1.text.toString().trim()
            StaticBusinessRegistration.bActualadress = Bactualadress
            val Bbillingadress = busi_signup2_edittext2.text.toString().trim()
            StaticBusinessRegistration.Bbillingadress = Bbillingadress
            //
            val Bphone = busi_signup2_edittext3.text.toString().trim()
            val busicountryCode = cppfield2.selectedCountryCode
            val busimobilenum = "+" + busicountryCode + Bphone
            StaticBusinessRegistration.BphoneNo = busimobilenum
            //
            val Bcountry = busi_signup2_edittext4.selectedCountryName
            StaticBusinessRegistration.Bcountryy = Bcountry
            val Bpass = busi_signup2_inputedittext5b.text.toString().trim()
            StaticBusinessRegistration.Bpassw = Bpass
            BthirdPg()
        }
    }

    private fun validateBActualAddress(): Boolean {
        val BactualAdress = busi_signup2_edittext1.text.toString().trim { it <= ' ' }
        return if (BactualAdress.isEmpty()) {
            busi_signup2_edittext1.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup2_edittext1.error = null
            true
        }
    }


    private fun validateBBillingAddress(): Boolean {
        val BbillingAdress = busi_signup2_edittext2.text.toString().trim { it <= ' ' }
        return if (BbillingAdress.isEmpty()) {
            busi_signup2_edittext2.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup2_edittext2.error = null
            true
        }
    }

    private fun validateBphoneNum(): Boolean {
        val bphoneNum = busi_signup2_edittext3.text.toString().trim { it <= ' ' }
        return if (bphoneNum.isEmpty()) {
            busi_signup2_edittext3.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup2_edittext3.error = null
            true
        }
    }


    private fun validateBpass(): Boolean {
        val bpass = busi_signup2_inputedittext5b.text.toString().trim { it <= ' ' }
        return if (bpass.isEmpty()) {
            busi_signup2_inputedittext5b.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup2_inputedittext5b.error = null
            true
        }
    }

    //confirmpass validation
    private fun validateBSignupConfrmPassword(): Boolean {
        val passwordInput = busi_signup2_inputedittext5b.text.toString().trim { it <= ' ' }
        val confrmpassInput = busi_signup2_inputedittext6b.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            busi_signup2_inputedittext6b.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            busi_signup2_inputedittext6b.error = getString(R.string.Passworddoesnotmatch)
            false
        } else {
            busi_signup2_inputedittext6b.error = null
            true
        }
    }


}