package com.malka.androidappp.newPhase.presentation.dialogsShared

import android.content.Context
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import kotlinx.android.synthetic.main.dialog_pick_image_methods.*

class PickImageMethodsDialog(
    context: Context, private var showDeleteImage: Boolean,
    private var onAttachedImageMethodSelected: OnAttachedImageMethodSelected
) : BaseDialog(context) {


    override fun getViewId(): Int = R.layout.dialog_pick_image_methods

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true


//    override fun getViewId(): Int = R.layout.dialog_attached_file


    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        if (showDeleteImage) {
            btnDeleteImage.show()
        } else {
            btnDeleteImage.hide()
        }
        btnSelectGallery.setOnClickListener {
            onAttachedImageMethodSelected.setOnAttachedImageMethodSelected(ConstantObjects.FILES)
            dismiss()
        }
        btnSelectCamera.setOnClickListener {
            onAttachedImageMethodSelected.setOnAttachedImageMethodSelected(ConstantObjects.CAMERA)
            dismiss()
        }
        btnDeleteImage.setOnClickListener {
            onAttachedImageMethodSelected.onDeleteImage()
            dismiss()
        }
    }

    interface OnAttachedImageMethodSelected {
        fun setOnAttachedImageMethodSelected(attachedMethod: Int)
        fun onDeleteImage()
    }

}