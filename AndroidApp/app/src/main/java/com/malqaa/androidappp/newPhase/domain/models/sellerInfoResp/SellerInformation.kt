package com.malqaa.androidappp.newPhase.domain.models.sellerInfoResp

import android.os.Parcelable
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.Branch
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellerInformation(
    val providerId: String?=null,
    val rate: Float,
    val businessAccountId: String?=null,
    var showUserInformation: String?=null,
    val image: String? = null,
    val name: String? = null,
    val city: String? = null,
    val phone: String? = null,
    val instagram: String? = null,
    val youTube: String? = null,
    val skype: String? = null,
//    "webSite": null,
    val faceBook: String? = null,
    val twitter: String? = null,
    val linkedIn: String? = null,
    val snapchat: String? = null,
    val tikTok: String? = null,
    val createdAt: String? = null,
    val lat:Double?=null,
    val lon:Double?=null,
    var isFollowed: Boolean,
    var acceptQuestion: Boolean,
    var branches: ArrayList<Branch>
):Parcelable