package com.malqaa.androidappp.newPhase.core

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<VB : ViewBinding>(context: Context) : Dialog(context) {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Use the abstract function `inflateViewBinding` to get the specific view binding instance
        binding = inflateViewBinding()
        setContentView(binding.root)

        // Set dialog to full screen or wrap content
        if (isFullScreen()) {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        } else {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        window?.setGravity(Gravity.CENTER)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (isLoadingDialog()) {
            window?.setDimAmount(0.0f)
        }

        // Handle the cancelable property
        if (isCancelable()) {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        } else {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        // Call initialization method from subclass
        initialization()
    }

    // Abstract methods to be implemented in subclasses
    abstract fun inflateViewBinding(): VB // Abstract function to provide ViewBinding instance
    abstract fun isFullScreen(): Boolean
    abstract fun isCancelable(): Boolean
    abstract fun initialization()
    abstract fun isLoadingDialog(): Boolean
}
