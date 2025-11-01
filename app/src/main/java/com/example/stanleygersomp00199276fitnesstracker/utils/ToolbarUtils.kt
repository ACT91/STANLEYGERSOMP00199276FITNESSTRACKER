package com.example.stanleygersomp00199276fitnesstracker.utils

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.appbar.MaterialToolbar

object ToolbarUtils {
    fun tintToolbarIconsWhite(toolbar: MaterialToolbar) {
        // Tint navigation icon
        try {
            toolbar.navigationIcon?.let { nav ->
                val wrapped: Drawable = DrawableCompat.wrap(nav)
                DrawableCompat.setTint(wrapped, 0xFFFFFFFF.toInt())
                toolbar.navigationIcon = wrapped
            }
        } catch (_: Exception) {
        }

        // Tint overflow icon (three-dots)
        try {
            toolbar.overflowIcon?.let { overflow ->
                val wrapped = DrawableCompat.wrap(overflow)
                DrawableCompat.setTint(wrapped, 0xFFFFFFFF.toInt())
                toolbar.overflowIcon = wrapped
            }
        } catch (_: Exception) {
        }

        // Tint menu icons
        try {
            val menu = toolbar.menu
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                val icon = item.icon
                if (icon != null) {
                    val wrapped = DrawableCompat.wrap(icon)
                    DrawableCompat.setTint(wrapped, 0xFFFFFFFF.toInt())
                    item.icon = wrapped
                }
            }
        } catch (_: Exception) {
        }
    }
}
