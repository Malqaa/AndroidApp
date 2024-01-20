package com.malqaa.androidappp.newPhase.domain.models

import android.net.Uri

data class ImageSelectModel(
    val uri: Uri? = null,
    val base64: String = "",
    var is_main: Boolean = false,
    var url: String = "",
    var isEdit: Boolean = false,
    val id :Int=0,
    val type: Int=0,
    var isMainMadia: Boolean=false
)