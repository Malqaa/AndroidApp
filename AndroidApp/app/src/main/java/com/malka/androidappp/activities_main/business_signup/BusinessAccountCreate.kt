package com.malka.androidappp.botmnav_fragments.activities_main.business_signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_business_signup_pg2.*

class BusinessAccountCreate : AppCompatActivity() {
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
        val BactualAdress = busi_signup2_edittext1.text.toString().trim { it <= ' ' }
        return if (BactualAdress.isEmpty()) {
            busi_signup2_edittext1.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            busi_signup2_edittext1.error = null
            true
        }
    }



}