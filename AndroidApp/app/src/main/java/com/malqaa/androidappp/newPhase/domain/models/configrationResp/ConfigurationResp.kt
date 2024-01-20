package com.malqaa.androidappp.newPhase.domain.models.configrationResp

import com.google.gson.annotations.SerializedName

data class ConfigurationResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val configurationData: ConfigurationData? = null
)

data class ConfigurationData(
    val configId: Int,
    val configKey: String,
    val configValue: String,
    val confingDescription : String
)
