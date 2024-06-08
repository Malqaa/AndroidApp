package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Context
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton
import kotlinx.android.synthetic.main.activity_play.fbButtonBack
import kotlinx.android.synthetic.main.dialog_image.imageLarge

class OpenImgLargeDialog(context: Context, var imageUri:String) : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image)

        PicassoSingleton.getPicassoInstance().load(imageUri).into(imageLarge)

//        andExoPlayerView.setSource(uri)
    }

//    override fun getViewId(): Int = R.layout.dialog_image
//
//
//    override fun isFullScreen(): Boolean = false
//
//    override fun isCancelable(): Boolean = true
//
//
//    override fun isLoadingDialog(): Boolean = false
//
//    override fun initialization() {
//        PicassoSingleton.getPicassoInstance().load(imageUri).into(imageLarge)
//
//
//    }

}