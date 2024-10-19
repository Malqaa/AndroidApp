package com.malqaa.androidappp.newPhase.presentation.activities.signup.activity1

import android.content.Context
import com.malqaa.androidappp.databinding.DialogTermsBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects

class TermsDialog(
    context: Context,
    var listener: AcceptTermsListener
) : BaseDialog<DialogTermsBinding>(context) {

    override fun inflateViewBinding(): DialogTermsBinding {
        return DialogTermsBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        binding.btnAccept.setOnClickListener {
            ConstantObjects.acceptTerms = true
            listener.onAccept()
            dismiss()
        }

    }

    interface AcceptTermsListener {
        fun onAccept()
    }
}