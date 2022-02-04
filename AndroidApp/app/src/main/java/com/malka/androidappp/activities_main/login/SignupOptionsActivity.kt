package com.malka.androidappp.activities_main.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.signup_account.signup_pg1.SignupPg1
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.BusinessSignupPg1
import kotlinx.android.synthetic.main.activity_sign_up_options.*


open class SignupOptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_options)
        supportActionBar?.hide()

        btn_signup_normal.setOnClickListener() {
            val intent = Intent(this@SignupOptionsActivity, SignupPg1::class.java)
            startActivity(intent)
        }

        btn_signup_business.setOnClickListener() {
            val intent = Intent(this@SignupOptionsActivity, BusinessSignupPg1::class.java)
            startActivity(intent)
        }
    }


}
