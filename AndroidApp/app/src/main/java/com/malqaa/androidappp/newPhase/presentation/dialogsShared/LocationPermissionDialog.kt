package com.malqaa.androidappp.newPhase.presentation.dialogsShared

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.malqaa.androidappp.BuildConfig
import com.malqaa.androidappp.databinding.DialogLocationPermissionBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog

class LocationPermissionDialog(context: Context) :
    BaseDialog<DialogLocationPermissionBinding>(context) {

    override fun inflateViewBinding(): DialogLocationPermissionBinding {
        return DialogLocationPermissionBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = false
    override fun isLoadingDialog(): Boolean = false


    override fun initialization() {
        binding.btnOpen.setOnClickListener {
            dismiss()
            openLocationPermissionScreen()
        }
    }

    private fun openLocationPermissionScreen() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        )
        context.startActivity(intent)
    }

}