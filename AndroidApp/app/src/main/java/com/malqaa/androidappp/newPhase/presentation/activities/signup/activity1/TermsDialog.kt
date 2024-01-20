package com.malqaa.androidappp.newPhase.presentation.activities.signup.activity1

import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects

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