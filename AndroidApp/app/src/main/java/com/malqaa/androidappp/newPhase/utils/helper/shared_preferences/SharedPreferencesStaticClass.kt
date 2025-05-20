package com.malqaa.androidappp.newPhase.utils.helper.shared_preferences

import io.paperdb.Paper

class SharedPreferencesStaticClass {
    companion object {
        var ad_userid = ""
        var myFeedback = false
        var islogin = "islogin"
        var isNotifyEnable = "isNotifyEnable"
        var user_object = "user_object"
        var masterCardIdKey = "master"
        var cartCount = "cartCount"
        var addressTitleAdded = "addressTitleAdded"
        var assignCartToUserKey = "assignCartToUserKey"

        fun saveFcmToken(token:String){
            Paper.book().write("fcmToken", token)
        }
        fun getFcmToken(): String {
            return Paper.book().read<String>("fcmToken") ?: ""
        }
        fun saveAccountId(id:Int){
            Paper.book().write("accountId", id)
        }
        fun getAccountId(): Int {
            return Paper.book().read<Int>("accountId") ?: 1
        }
        fun saveSwitchNotify(notifyEnable: Boolean) {
            Paper.book().write(isNotifyEnable, notifyEnable)
        }
        fun getSwitchNotify(): Boolean {
            return Paper.book().read<Boolean>(isNotifyEnable) ?: false
        }

        fun saveMasterCartId(cardId: String) {
            Paper.book().write(masterCardIdKey, cardId)
        }

        fun saveShowUserInformation(value: Int) {
            Paper.book().write("showUserInformation", value)
        }
        fun getShowUserInformation(): Int {
            return Paper.book().read<Int>("showUserInformation") ?: 0
        }
        fun getMasterCartId(): String {
            return Paper.book().read<String>(masterCardIdKey) ?: "0"
        }
        fun saveCartCount(cardId: Int) {
            Paper.book().write(cartCount, cardId)
        }

        fun getCartCount(): Int {
            return Paper.book().read<Int>(cartCount) ?: 0
        }

        fun clearCartCount() {
            Paper.book().delete(cartCount)
        }

        fun removeItemCart(total:Int) {
            Paper.book().write(cartCount, total)
        }
        fun saveAddressTitle(addressTitle: String) {
            Paper.book().write(addressTitleAdded, addressTitle)
        }

        fun getAddressTitle(): String {
            return Paper.book().read<String>(addressTitleAdded) ?: ""
        }

        fun clearAddressTitle() {
            Paper.book().delete(addressTitleAdded)
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