package com.malka.androidappp.helper

import android.view.View

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.hideLoader(){
    this.visibility = View.GONE
}
fun View.showLoader(){
    this.visibility = View.VISIBLE
}
