package com.malka.androidappp.helper

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.malka.androidappp.R

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}
