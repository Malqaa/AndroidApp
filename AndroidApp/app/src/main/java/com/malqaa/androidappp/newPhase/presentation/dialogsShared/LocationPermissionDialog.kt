package com.malqaa.androidappp.newPhase.presentation.dialogsShared

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.malqaa.androidappp.BuildConfig
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import kotlinx.android.synthetic.main.dialog_location_permission.*

class LocationPermissionDialog(context: Context): BaseDialog(context) {


    override fun getViewId(): Int = R.layout.dialog_location_permission

    override fun isFullScreen(): Boolean =false

    override fun isCancelable(): Boolean =false
    override fun isLoadingDialog(): Boolean = false



    override fun initialization() {
        btnOpen.setOnClickListener {
            dismiss()
            openLocationPermissionScreen()
        }
//        viewBinding.btnCancel.setOnClickListener {
//            dismiss()
//        }
    }



    private fun openLocationPermissionScreen(){
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + BuildConfig.APPLICATION_ID))
        context.startActivity(intent)
    }


}