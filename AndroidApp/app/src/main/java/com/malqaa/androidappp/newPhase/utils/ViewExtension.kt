package com.malqaa.androidappp.newPhase.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}
/**
 * Avoid boilerplate code when inflating layout
 * @param layout Layout resource
 * @return Inflated view
 */
fun ViewGroup.inflateCustomeLayout(@LayoutRes layout: Int) =
    LayoutInflater.from(context).inflate(layout, this, false)

/**
 * Set LinearLayoutManager to RecyclerView
 * @param orientation (VERTICAL or HORIZONTAL)
 */
fun RecyclerView.linearLayoutManager(@RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL): RecyclerView.LayoutManager? {
    layoutManager = LinearLayoutManager(this.context, orientation, false)
    return layoutManager
}

/**
 * Dismiss keyboards
 * @param view View of current focus
 */
fun Context.hideKeyboard(view: View?) = view?.let {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

/**
 * Get color from resources
 * @param colorId Id of color which will load
 * @return Chosen color
 */
fun Context.getColorCompat(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

/**
 * Get drawable from resources
 * @param drawableId Id of drawable which will load
 * @return Chosen drawable
 */
fun Context.getDrawableCompat(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}
