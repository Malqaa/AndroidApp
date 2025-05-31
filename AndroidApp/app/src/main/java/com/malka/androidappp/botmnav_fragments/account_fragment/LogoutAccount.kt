package com.malka.androidappp.botmnav_fragments.account_fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.signup_account.signup_pg1.SignupPg1
import kotlinx.android.synthetic.main.fragment_logout_account.*
import kotlinx.android.synthetic.main.fragment_logout_notification.*


class LogoutAccount : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_logout_account.setTitle("Account")
        toolbar_logout_account.setTitleTextColor(Color.WHITE)

        ////////////////////////////////////////////////////
        val clickspan = object : ClickableSpan() {

            //It removes underline from clickablespan
            override fun updateDrawState(ds: TextPaint) { // override updateDrawState
                ds.isUnderlineText = false // set to false to remove underline
            }
            override fun onClick(view: View) {
                val intent = Intent(requireContext(), SignupPg1::class.java)
                startActivity(intent)
            }

        }
        /////////////////////////ClickableSpan for Signup////////////////////////////////
        val myId = tx_acc3.text.toString().trim()
        val mSpannableString  = SpannableString(myId)
        mSpannableString.setSpan(clickspan, 22, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mSpannableString.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.greenspan)), 22, 31, 0)
        // make text bold
        mSpannableString.setSpan(StyleSpan(Typeface.BOLD), 22, 31, 0)
        tx_acc3.movementMethod = LinkMovementMethod.getInstance()
        tx_acc3.setText(mSpannableString)


    }
}