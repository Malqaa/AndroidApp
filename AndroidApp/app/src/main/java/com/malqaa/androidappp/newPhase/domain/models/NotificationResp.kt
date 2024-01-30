package com.malqaa.androidappp.newPhase.domain.models

data class NotificationResp(
    val status_code:Int,
    val status:String,
    val message :String,
    val data :ArrayList<NotifyOut>
)


