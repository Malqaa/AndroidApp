package com.malka.androidappp.newPhase.data.helper.shared_preferences

import io.paperdb.Paper

class SharedPreferencesStaticClass {
    companion object {
        var ad_userid = ""
        var myFeedback = false
        var islogin = "islogin"
        var user_object = "user_object"
        var masterCardIdKey = "master"
        var assignCartToUserKey = "assignCartToUserKey"
        fun saveMasterCartId(cardId: String) {
            Paper.book().write(masterCardIdKey, cardId)
        }

        fun getMasterCartId(): String {
            return Paper.book().read<String>(masterCardIdKey) ?: "0"
        }
        fun clearCardMasterId() {
            Paper.book().delete(masterCardIdKey)
        }
//        fun saveAssignCartToUser(b: Boolean) {
//            Paper.book().write(assignCartToUserKey, b)
//        }
//
//        fun getAssignCartToUser(): Boolean {
//            return Paper.book().read<Boolean>(assignCartToUserKey, false) ?: false
//        }
//
//        fun clearAssignCartToUser() {
//            Paper.book().delete(assignCartToUserKey)
//        }


    }
}