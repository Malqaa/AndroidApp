package com.malqaa.androidappp.newPhase.presentation.dialogsShared

import android.content.Context
import com.malqaa.androidappp.databinding.DialogPickImageMethodsBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class PickImageMethodsDialog(
    context: Context, private var showDeleteImage: Boolean,
    private var onAttachedImageMethodSelected: OnAttachedImageMethodSelected
) : BaseDialog<DialogPickImageMethodsBinding>(context) {

    override fun inflateViewBinding(): DialogPickImageMethodsBinding {
        return DialogPickImageMethodsBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true

    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        if (showDeleteImage) {
            binding.btnDeleteImage.show()
        } else {
            binding.btnDeleteImage.hide()
        }
        binding.btnSelectGallery.setOnClickListener {
            onAttachedImageMethodSelected.setOnAttachedImageMethodSelected(ConstantObjects.FILES)
            dismiss()
        }
        binding.btnSelectCamera.setOnClickListener {
            onAttachedImageMethodSelected.setOnAttachedImageMethodSelected(ConstantObjects.CAMERA)
            dismiss()
        }
        binding.btnDeleteImage.setOnClickListener {
            onAttachedImageMethodSelected.onDeleteImage()
            dismiss()
        }
    }

    interface OnAttachedImageMethodSelected {
        fun setOnAttachedImageMethodSelected(attachedMethod: Int)
        fun onDeleteImage()
    }

}