package com.malka.androidappp.servicemodels.feedbacks

import com.malka.androidappp.servicemodels.BaseModel

class FeedbackObject(
    status_code: Int,
    message: String,
    val data: MutableList<FeedbackProperties>,
    var Buying: MutableList<FeedbackProperties>,
    var Selling: MutableList<FeedbackProperties>,
    var neutral_count: Int = 0,
    var negative_count: Int = 0,
    var positive_count: Int = 0

) :
    BaseModel(
        status_code, message
    )
