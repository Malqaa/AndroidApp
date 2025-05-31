package com.malqaa.androidappp.newPhase.utils

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.material.badge.BadgeDrawable
import com.malqaa.androidappp.R

object BadgeUtils {
    fun showBadge(context: Context, menuItem: MenuItem, count: Int) {
        val badgeDrawable = if (count > 0) {
            BadgeDrawable.create(context)
                .apply {
                    number = count
                    isVisible = true
                    backgroundColor = ContextCompat.getColor(context, R.color.red)
                }
        } else {
            null
        }
        BadgeUtils.attachBadgeToMenuIcon(menuItem, badgeDrawable)
    }

    private fun attachBadgeToMenuIcon(menuItem: MenuItem, badgeDrawable: BadgeDrawable?) {
        val iconView = menuItem.icon as? LayerDrawable
        if (iconView != null) {
            if (badgeDrawable != null) {
                iconView.mutate()
                val badge = iconView.findDrawableByLayerId(R.drawable.ic_badge)
                if (badge != null && badge is BadgeDrawable) {
                    iconView.setDrawableByLayerId(R.drawable.ic_badge, badgeDrawable)
                } else {
                    iconView.setDrawableByLayerId(R.drawable.ic_badge, badgeDrawable)
                }
            } else {
                iconView.setDrawableByLayerId(R.drawable.ic_badge, null)
            }
        }
    }
}
