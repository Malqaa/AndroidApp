package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.technicalSupportActivity.addTechencalSupport

import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogContactUsTypeBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.HelpFunctions


class ContactUsTypeDialog(
    context: Context,
    private var setOnSelectCommunicationType: SetOnSelectCommunicationType
) : BaseDialog<DialogContactUsTypeBinding>(context) {

    override fun inflateViewBinding(): DialogContactUsTypeBinding {
        return DialogContactUsTypeBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false
    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.btnSave.setOnClickListener {
            if (binding.tvSuggestions.isChecked) {
                setOnSelectCommunicationType.onSelectCommunicationType(1)
                dismiss()
            } else if (binding.tvComplaint.isChecked) {
                setOnSelectCommunicationType.onSelectCommunicationType(2)
                dismiss()
            } else {
                HelpFunctions.ShowLongToast(
                    context.getString(R.string.select_type_of_communication),
                    context
                )
            }
        }
    }

    interface SetOnSelectCommunicationType {
        fun onSelectCommunicationType(position: Int)
    }
}