package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.technicalSupportActivity.addTechencalSupport

import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import kotlinx.android.synthetic.main.dialog_contact_us_type.*


class ContactUsTypeDialog(context: Context, private var setOnSelectCommunicationType: SetOnSelectCommunicationType) :
    BaseDialog(context){
    override fun getViewId(): Int = R.layout.dialog_contact_us_type

    override fun isFullScreen(): Boolean =false

    override fun isCancelable(): Boolean=true
    override fun isLoadingDialog(): Boolean  =false
    override fun initialization() {
        ivClose.setOnClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            if(tvSuggestions.isChecked){
                setOnSelectCommunicationType.onSelectCommunicationType(1)
                dismiss()
            }else if(tvComplaint.isChecked){
                setOnSelectCommunicationType.onSelectCommunicationType(2)
                dismiss()
            }else{
                HelpFunctions.ShowLongToast(context.getString(R.string.select_type_of_communication),context)
            }
        }
    }

    interface SetOnSelectCommunicationType{
      fun  onSelectCommunicationType(position:Int)
    }


}