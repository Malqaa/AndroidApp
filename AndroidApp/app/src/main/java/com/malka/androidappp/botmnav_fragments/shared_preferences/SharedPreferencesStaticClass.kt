package com.malka.androidappp.botmnav_fragments.shared_preferences

class SharedPreferencesStaticClass {
    companion object{
         val SHARED_PREFS = "sharedPrefs"
         val TEXT = "text1"
         val TEXT2 = "text2"

        ///////////For Ads detail///////////////
        var ad_userid = ""
        var is_selected_ans_or_comment= ""
        var getReqQuestionId_toReplyAns = ""
        var getReqQuestionId_toComment = ""

        // For user feedback
        var myFeedback = false
    }
}