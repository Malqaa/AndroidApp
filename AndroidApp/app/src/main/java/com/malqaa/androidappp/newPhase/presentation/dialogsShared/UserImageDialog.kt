package com.malqaa.androidappp.newPhase.presentation.dialogsShared

import android.content.Context
import android.view.View
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogUserImageBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog

class UserImageDialog(context: Context, var setOnImageSelectListeners: SetOnImageSelectListeners) :
    BaseDialog<DialogUserImageBinding>(context),
    View.OnClickListener {

    override fun initialization() {
        binding.btnTakePhoto.setOnClickListener(this)
        binding.btnDeletePhoto.setOnClickListener(this)
    }

    override fun inflateViewBinding(): DialogUserImageBinding {
        return DialogUserImageBinding.inflate(layoutInflater)
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


    interface SetOnImageSelectListeners {
        fun onImageSelectListeners(position: Int)
    }

}