package com.malka.androidappp.botmnav_fragments.home.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class CarAdvertisment(
    val city: String,
    val country: String,
    val enddate: Any,
    val expiredate: Any,
    val id: String,
    val homepageImage: String,
    val listingtitle: Any,
    val name: String,
    val phone: Any,
    val price: String,

    val producttitle: Any,
    val publishdate: Any,

    val publishtype: Any,

    val quanitty: Any,

    val referenceId: String,

    val slug: String,

    val state: Any,

    val template: String,

    val user: String
)