package com.jssk.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle

class JSSKApp: Application() {
    private val activities = arrayListOf("")

    override fun onCreate() {
        super.onCreate()
        context = this
        registerActivityLifecycleCallbacks(object : ActivityLifeCycle() {
            @SuppressLint("SourceLockedOrientationActivity")
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                try {
                    // if(!activities.contains(activity.localClassName))
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        })
    }

    companion object {
        lateinit var context: Context
        const val tag = "JSSK_APP_LOGS"
    }
}