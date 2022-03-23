package com.malka.androidappp.botmnav_fragments.activities_main.business_signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.design.Models.BusinessUserModel
import com.malka.androidappp.design.Switch_Account
import com.malka.androidappp.design.add_address
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.addBusinessUserlistReponse
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_business_signup_pg2.*
import kotlinx.android.synthetic.main.activity_business_signup_pg2.textEmaill
import kotlinx.android.synthetic.main.activity_business_signup_pg2.userNamee
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import kotlinx.android.synthetic.main.add_account_layout.*
import kotlinx.android.synthetic.main.carspec_card8.*
import kotlinx.android.synthetic.main.fragment_add_photo.*

class BusinessAccountCreate : BaseActivity()  {
    var youtube=false
    var twitter=false
    var whatsApp=false
    var snapShot=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup_pg2)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        busi_signup2_btn.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        business_signup2_button.setOnClickListener() {

            if (validateSignupEmail()  && validateCompanyName() && validateCompanyURL()) {

                addBusinessUser()

            }



        }

        ic_youtube_select.setOnClickListener {
            youtube=!youtube
            if(youtube){
                ic_youtube_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_youtube.show()
            }else{
                ic_youtube_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_youtube.hide()
            }

        }
        ic_twitter_select.setOnClickListener {
            twitter=!twitter
            if(twitter){
                ic_twitter_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_twitter.show()
            }else{
                ic_twitter_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_twitter.hide()
            }

        }
        ic_whatsapp_select.setOnClickListener {
            whatsApp=!whatsApp
            if(whatsApp){
                ic_whatsapp_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_whatsapp.show()
            }else{
                ic_whatsapp_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_whatsapp.hide()
            }

        }
        ic_snapshot_select.setOnClickListener {
            snapShot=!snapShot
            if(snapShot){
                ic_snapshot_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_snapshot.show()
            }else{
                ic_snapshot_select.background= ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_snapshot.hide()
            }

        }
    }

    fun BthirdPg() {
        val intent = Intent(this@BusinessAccountCreate, BusinessSignupPg3::class.java)
        startActivity(intent)
        if (ConstantObjects.currentLanguage == "en") {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }



    private fun validateBActualAddress(): Boolean {
        val BactualAdress = textEmaill.text.toString().trim { it <= ' ' }
        return if (BactualAdress.isEmpty()) {
            textEmaill.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textEmaill.error = null
            true
        }
    }





    fun addBusinessUser() {

        HelpFunctions.startProgressBar(this)

        val malqa = RetrofitBuilder.GetRetrofitBuilder()

        val UserNamee = userNamee.text.toString()
        val CompanyName = company_name.text.toString()
        val EmailAddress = textEmaill.text.toString()
        val WebsiteName = busi_signup3_edittext2.text.toString()
        val FacebookLink = Facebook.text.toString()
        val InstagramLink = Instagram.text.toString()
        val youtubeLink = ic_youtube.text.toString()
        val TwitterLink = ic_twitter.text.toString()
        val WhatsappNumber = ic_whatsapp.text.toString()
        val SnapchatLInk = ic_snapshot.text.toString()
        val SelectEmployemntType = employment_type.text.toString()
        val CommercialRegisterNo = commercial_registration_no.text.toString()
        val SelectDate = date.text.toString()
        val TaxNumber = TaxNumber.text.toString()


        val addBusinessUser = BusinessUserModel.getBusinessList(

            businessName = CompanyName,
            businessEmail = EmailAddress,
            businessURL = WebsiteName,
            businessFacebookURI = FacebookLink,
            businessInstagramURI = InstagramLink,
            businessYoutubeURI = youtubeLink,
            businessTwitterURI = TwitterLink,
            businessWatsappNumber = WhatsappNumber,
            businessSnapchatURI = SnapchatLInk,
            businessType = SelectEmployemntType,
            businessRegistrationNumber = CommercialRegisterNo,
            businessRegistrationExpiry = SelectDate,
            approvedBy = "",
            approvedOn = "",
            businessGoogleLatLng = "",
            businessLogoPath = "",
            businessPhone = "12345678",
            id = 0,
            isApproved = true,
            userId = ConstantObjects.logged_userid

        )
        val call: retrofit2.Call<addBusinessUserlistReponse> = malqa.addBusinesUser(addBusinessUser)

        call?.enqueue(object : retrofit2.Callback<addBusinessUserlistReponse?> {
            override fun onFailure(
                call: retrofit2.Call<addBusinessUserlistReponse?>?,
                t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@BusinessAccountCreate, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: retrofit2.Call<addBusinessUserlistReponse?>,
                response: retrofit2.Response<addBusinessUserlistReponse?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: addBusinessUserlistReponse = response.body()!!
                        if (respone.status_code.equals("200")) {
                            BusinessAddUser()
                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }

    fun BusinessAddUser() {
        val intent = Intent(this, Switch_Account::class.java)
        startActivity(intent)
    }





    //Email Validation
    private fun validateSignupEmail(): Boolean {
        val emailInput =
            textEmaill!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {

            showError(getString(R.string.Please_enter,getString(R.string.Email)))
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            showError(getString(R.string.Pleaseenteravalidemailaddress))
            false
        } else {

            true
        }
    }

    //CompanyName Validation
    private fun validateCompanyName(): Boolean {
        val emailInput =
            company_name!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter,getString(R.string.the_company_s_name)))
            false
        } else {
            company_name!!.error = null
            true
        }
    }



    //WebsiteURL Validation
    private fun validateCompanyURL(): Boolean {
        val emailInput =
            busi_signup3_edittext2!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter,getString(R.string.website)))
            false
        } else {
            true
        }
    }









}