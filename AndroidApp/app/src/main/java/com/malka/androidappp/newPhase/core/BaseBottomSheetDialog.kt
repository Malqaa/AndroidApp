package com.malka.androidappp.newPhase.core


import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malka.androidappp.R

abstract class BaseBottomSheetDialog(context: Context) :
    BottomSheetDialog(context, R.style.bottomSheetDialogTheme) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        initialization()
    }

    abstract fun setViewBinding()
    abstract fun initialization()


}