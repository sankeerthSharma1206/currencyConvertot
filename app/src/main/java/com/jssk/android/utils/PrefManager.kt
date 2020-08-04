package com.jssk.android.utils

import android.content.Context
import android.content.SharedPreferences
import com.jssk.android.dtos.UserDTO

object PrefManager {
    private const val appPreferences = "JSSK App Preferences"
    private const val userPreferences = "JSSK User Preferences"

    private fun getUserSharedPreferences(): SharedPreferences? {
        return JSSKApp.context.getSharedPreferences(userPreferences, Context.MODE_PRIVATE)
    }

    fun getAppSharedPreferences(): SharedPreferences? {
        return JSSKApp.context.getSharedPreferences(appPreferences, Context.MODE_PRIVATE)
    }

    fun clearAppPreferences() {
        getAppSharedPreferences()?.edit()?.clear()?.apply()
    }

    fun clearUserPreferences() {
        getUserSharedPreferences()?.edit()?.clear()?.apply()
    }

    fun saveUserData(key: String, value: Any?) {
        val editor = getUserSharedPreferences()?.edit()
        when (value) {
            is String -> editor?.putString(key, value)
            is Boolean -> editor?.putBoolean(key, value)
            is Float -> editor?.putFloat(key, value)
            is Int -> editor?.putInt(key, value)
            is Long -> editor?.putLong(key, value)
        }
        editor?.apply()
    }

    fun saveAppData(key: String, value: Any?) {
        val editor = getAppSharedPreferences()?.edit()
        when (value) {
            is String -> editor?.putString(key, value)
            is Boolean -> editor?.putBoolean(key, value)
            is Float -> editor?.putFloat(key, value)
            is Int -> editor?.putInt(key, value)
            is Long -> editor?.putLong(key, value)
        }
        editor?.apply()
    }

    fun getAppStringData(key: String): String? {
        return getAppSharedPreferences()?.getString(key, null)
    }

    fun getUserStringData(key: String): String? {
        return getUserSharedPreferences()?.getString(key, null)
    }

    fun getUserIntData(key: String): Int? {
        return getUserSharedPreferences()?.getInt(key, 0)
    }

    fun getUserFloatData(key: String): Float? {
        return getUserSharedPreferences()?.getFloat(key, 0f)
    }

    fun getUserLongData(key: String): Long? {
        return getUserSharedPreferences()?.getLong(key, 0L)
    }

    fun getAppIntData(key: String): Int? {
        return getAppSharedPreferences()?.getInt(key, 0)
    }

    fun getUserBooleanData(key: String): Boolean {
        return getUserSharedPreferences()?.getBoolean(key, false) ?: false
    }

    fun getAppBooleanData(key: String): Boolean {
        return getAppSharedPreferences()?.getBoolean(key, false) ?: false
    }

    fun removeAppData(key: String) {
        getAppSharedPreferences()?.edit()?.remove(key)?.apply()
    }

    fun removeUserData(key: String) {
        getUserSharedPreferences()?.edit()?.remove(key)?.apply()
    }

    fun saveUserDTOData(user: UserDTO) {
        val pref = getUserSharedPreferences()?.edit()
        pref?.apply()
    }
}