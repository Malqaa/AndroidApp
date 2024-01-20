package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4

import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import kotlinx.android.synthetic.main.dialog_add_video_link.*

class DialogAddVideoLink(context: Context,var setSaveLinkListeners:SetSaveLinkListeners) : BaseDialog(context) {
    override fun getViewId(): Int {
        return R.layout.dialog_add_video_link
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true

    override fun initialization() {
        ivClose.setOnClickListener {
            dismiss()
        }
        complete_order_btn.setOnClickListener {
            if (etAddVideo.text.toString().trim()!="") {
                setSaveLinkListeners.saveLinkListeners(etAddVideo.text.toString().trim())
                dismiss()

            } else {
                etAddVideo.error =
                    context.getString(
                        R.string.please_enter_valid,
                        context.getString(R.string.video_link)
                    )
            }

        }
    }

    override fun isLoadingDialog(): Boolean = false
    interface SetSaveLinkListeners{
        fun saveLinkListeners(value:String)
    }
}