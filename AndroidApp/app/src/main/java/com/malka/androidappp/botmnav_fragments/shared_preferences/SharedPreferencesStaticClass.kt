package com.malka.androidappp.botmnav_fragments.shared_preferences

import android.widget.ImageView
import com.malka.androidappp.helper.BaseViewHolder

class SharedPreferencesStaticClass {
    companion object{
         val TEXT = "text1"

        ///////////For Ads detail///////////////
        var ad_userid = ""
        var is_selected_ans_or_comment= ""
        var getReqQuestionId_toReplyAns = ""
        var getReqQuestionId_toComment = ""

        // For user feedback
        var myFeedback = false
        var islogin = "islogin"
        var userData = "userData"
    }
}