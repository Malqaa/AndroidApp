package com.malka.androidappp.botmnav_fragments.home.model

import androidx.room.*

@Entity(tableName = "general_advertisement")
data class Generaladvetisement(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_id")
    val responseId: Int,

    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "end_date")
    val enddate: String,
    val expiredate: Any,

    @ColumnInfo(name = "adv_id")
    val id: String,

    @ColumnInfo(name = "homepageImage")
    val homepageImage: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    val phone: Any,

    @ColumnInfo(name = "price")
    val price: String,

    @ColumnInfo(name = "startingPrice")
    val startingPrice: String,

    val producttitle: Any,

    val publishdate: Any,

    val publishtype: Any,

    val quanitty: Any,

    @ColumnInfo(name = "reference_id")
    val referenceId: String,

    @ColumnInfo(name = "slug")
    val slug: String,

    @ColumnInfo(name = "state")
    val state: String,

    @ColumnInfo(name = "template")
    val template: String,

    @ColumnInfo(name = "user")
    val user: String
)