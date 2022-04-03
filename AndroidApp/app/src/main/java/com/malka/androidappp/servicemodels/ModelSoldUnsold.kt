package com.malka.androidappp.servicemodels

import com.google.gson.annotations.SerializedName

data class ModelSoldUnsold(
    val `data`: Data,
    val message: String,
    val status_code: Int
) {
    data class Data(

        @SerializedName("solditems") var solditems_: List<AdDetailModel>,

        val solditemscount: Int,

        @SerializedName("unsolditems") var unsolditems_: List<AdDetailModel>,

        val unsolditemscount: Int,

        @SerializedName("sellingitems") var sellingitems_: List<AdDetailModel>,
        val sellingitemscount: Int
    ) {
        val solditems: List<AdDetailModel>
            get() {
                if (solditems_ == null) {
                    solditems_ = ArrayList()
                }
                return solditems_
            }
        val unsolditems: List<AdDetailModel>
            get() {
                if (unsolditems_ == null) {
                    unsolditems_ = ArrayList()
                }
                return unsolditems_
            }


        val sellingitems: List<AdDetailModel>
            get() {
                if (sellingitems_ == null) {
                    sellingitems_ = ArrayList()
                }
                return sellingitems_
            }
    }
}