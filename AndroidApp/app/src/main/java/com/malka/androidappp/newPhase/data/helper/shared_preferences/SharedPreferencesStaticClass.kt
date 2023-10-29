package com.malka.androidappp.newPhase.data.helper.shared_preferences

import io.paperdb.Paper

class SharedPreferencesStaticClass {
    companion object {
        var ad_userid = ""
        var myFeedback = false
        var islogin = "islogin"
        var user_object = "user_object"
        var masterCardIdKey = "master"
        var cartCount = "cartCount"
        var addressTitleAdded = "addressTitleAdded"
        var assignCartToUserKey = "assignCartToUserKey"
        fun saveMasterCartId(cardId: String) {
            Paper.book().write(masterCardIdKey, cardId)
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