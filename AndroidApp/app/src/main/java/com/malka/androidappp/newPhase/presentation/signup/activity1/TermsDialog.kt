package com.malka.androidappp.newPhase.presentation.signup.activity1

import android.content.Context
import android.widget.CompoundButton
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import kotlinx.android.synthetic.main.dialog_answer_question.tvQuestion

import kotlinx.android.synthetic.main.dialog_seller_filter_rate.*
import kotlinx.android.synthetic.main.dialog_terms.btnAccept

class TermsDialog(
    context: Context,
    var listener: AcceptTermsListener
) : BaseDialog(context) {


    override fun getViewId(): Int {
        return R.layout.dialog_terms
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        btnAccept.setOnClickListener {
            ConstantObjects.acceptTerms = true
            listener.onAccept()
            dismiss()
        }

    }


    interface AcceptTermsListener {
        fun onAccept()
    }
}