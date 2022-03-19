package com.malka.androidappp.servicemodels

import android.net.Uri

data class ImageSelectModel (val uri: Uri, val base64:String,var is_main:Boolean=false)