package com.malka.androidappp.newPhase.presentation.dialogsShared

import android.content.Context
import android.view.View
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import kotlinx.android.synthetic.main.dialog_user_image.*

class UserImageDialog(context: Context, var setOnImageSelectListeners: SetOnImageSelectListeners) : BaseDialog(context),
    View.OnClickListener {


    override fun getViewId(): Int {
        return R.layout.dialog_user_image
    }

    override fun initialization() {
        btnTakePhoto.setOnClickListener(this)
        btnDeletePhoto.setOnClickListener(this)
//        if (globalPreferences.getLang() == "ar") {
//            btnArabic.setTextColor(ContextCompat.getColor(context, R.color.blueDark))
//            btnEnglish.setTextColor(ContextCompat.getColor(context, R.color.black))
//        } else {
//            btnEnglish.setTextColor(ContextCompat.getColor(context, R.color.blueDark))
//            btnArabic.setTextColor(ContextCompat.getColor(context, R.color.black))
//        }
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun isCancelable(): Boolean {
        return true
    }

    override fun isLoadingDialog(): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTakePhoto -> {
                setOnImageSelectListeners.onImageSelectListeners(0)
                dismiss()
            }
            R.id.btnDeletePhoto -> {
                setOnImageSelectListeners.onImageSelectListeners(1)
                dismiss()
            }
        }
    }


    interface SetOnImageSelectListeners{
        fun onImageSelectListeners(position: Int)
    }

}