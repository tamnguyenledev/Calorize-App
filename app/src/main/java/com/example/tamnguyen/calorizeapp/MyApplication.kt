package com.example.tamnguyen.calorizeapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * Created by hoangdung on 11/27/17.
 */
class MyApplication : Application(){
    companion object {
        fun getNavigationBarHeight(context: Context, orientation: Int): Int {
            val resources = context.getResources()
            val id = resources.getIdentifier(
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape",
                    "dimen", "android")
            return if (id > 0) {
                resources.getDimensionPixelSize(id)
            } else 0
        }

        fun hasSoftNavBar(context: Context): Boolean {
            val id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android")
            return id > 0 && context.getResources().getBoolean(id)
        }

        fun getStatusBarHeight(context: Context): Int {
            var result = 0
            val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId)
            }
            return result
        }
    }

}