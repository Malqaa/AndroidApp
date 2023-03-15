package com.malka.androidappp.newPhase.core

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window

abstract class BaseDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getViewId())
        // we set this one if we need to enable our dialog to fill all the width and hight of the screen .
//        globalPreferences = new GlobalPreferences(getContext());
        if (isFullScreen()) {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        } else window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.CENTER)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (isLoadingDialog()) {
            window?.setDimAmount(0.0f)
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setTransitionBackgroundFadeDuration(5000);
//        }
        if (isCancelable()) {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        } else {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        initialization()
    }
    abstract fun getViewId(): Int
    abstract fun isFullScreen(): Boolean
    abstract fun isCancelable(): Boolean
    abstract fun initialization()
    abstract fun isLoadingDialog(): Boolean

}
