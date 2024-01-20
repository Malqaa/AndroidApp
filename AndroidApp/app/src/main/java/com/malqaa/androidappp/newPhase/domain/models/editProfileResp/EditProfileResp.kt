package com.malqaa.androidappp.newPhase.domain.models.editProfileResp

import com.google.gson.annotations.SerializedName

data class EditProfileResp(
    @SerializedName("data")
    val profileData: ProfileData,
    val message: String,
    val status: String,
    val status_code: Int
)