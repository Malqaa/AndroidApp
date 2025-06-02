package com.malka.androidappp.servicemodels.feedbacks

import org.joda.time.DateTime

data class FeedbackProperties(
    val _id: String,
    val loginUserId: String,
    val sellerId: String,
    val username: String = "username",
    val fullname: String = "fullname",
    val advId: String,
    val description: String,
    val rating: Int,
    val createdOn: String,
    val createdBy: String,
    val updateOn: String,
    val updatedBy: String,
    val isActive: Boolean
)