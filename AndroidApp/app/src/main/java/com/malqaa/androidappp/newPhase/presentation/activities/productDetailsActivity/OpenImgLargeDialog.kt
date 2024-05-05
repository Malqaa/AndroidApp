package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton
import kotlinx.android.synthetic.main.dialog_image.imageLarge

class OpenImgLargeDialog(context: Context, var imageUri:String) : BaseDialog(context) {

    override fun getViewId(): Int = R.layout.dialog_image


    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true


    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        PicassoSingleton.getPicassoInstance().load(imageUri).into(imageLarge)


    }

}