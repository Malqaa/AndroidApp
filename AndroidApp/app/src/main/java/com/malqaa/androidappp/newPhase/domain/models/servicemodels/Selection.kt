package com.malqaa.androidappp.newPhase.domain.models.servicemodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Selection (var name:String,var id:Int=0, var isSelected:Boolean=false): Parcelable