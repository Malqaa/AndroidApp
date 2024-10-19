package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4

import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogAddVideoLinkBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog

class DialogAddVideoLink(context: Context, var setSaveLinkListeners: SetSaveLinkListeners) :
    BaseDialog<DialogAddVideoLinkBinding>(context) {

    override fun inflateViewBinding(): DialogAddVideoLinkBinding {
        return DialogAddVideoLinkBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true

    override fun initialization() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.completeOrderBtn.setOnClickListener {
            if (binding.etAddVideo.text.toString().trim() != "") {
                setSaveLinkListeners.saveLinkListeners(binding.etAddVideo.text.toString().trim())
                dismiss()

            } else {
                binding.etAddVideo.error =
                    context.getString(
                        R.string.please_enter_valid,
                        context.getString(R.string.video_link)
                    )
            }

        }
    }

    override fun isLoadingDialog(): Boolean = false
    interface SetSaveLinkListeners {
        fun saveLinkListeners(value: String)
    }
}