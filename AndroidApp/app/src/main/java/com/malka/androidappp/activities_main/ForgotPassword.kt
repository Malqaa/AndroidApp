package com.malka.androidappp.activities_main

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val clicksignin = object : ClickableSpan() {
            //It removes underline from clickablespan
            override fun updateDrawState(ds: TextPaint) { // override updateDrawState
                ds.isUnderlineText = false // set to false to remove underline
            }
            override fun onClick(view: View) {
                val intent = Intent( this@ForgotPassword, SignInActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
        /////////////////////////ClickableSpan for Alreadyaccount////////////////////////////////
        val mSpannablerecover = SpannableString(getString(R.string.alreadyaccount))
        mSpannablerecover.setSpan(clicksignin,25 ,32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mSpannablerecover.setSpan(ForegroundColorSpan(getResources().getColor(R.color.greenspan)),25,32,0)
        mSpannablerecover.setSpan(StyleSpan(Typeface.BOLD), 25, 32, 0)
        textView23.movementMethod = LinkMovementMethod.getInstance()
        textView23.setText(mSpannablerecover)


        //data Validation
        emailtextt = findViewById(R.id.editText4)
    }

    private var emailtextt:EditText? = null

    //Data Validation
    private fun validateforgot(): Boolean {
        val Inputemail = emailtextt!!.text.toString().trim { it <= ' ' }

        return if (Inputemail.isEmpty()) {
            emailtextt!!.error = "Field can't be empty"
            false
        }
        else {
            emailtextt!!.error = null
            true
        }
    }

    fun forgotpassconfrm(v: View) {
        if (!validateforgot())
        {
            return
        }
        else
        {
            //signup3next(v)
        }

    }
}
