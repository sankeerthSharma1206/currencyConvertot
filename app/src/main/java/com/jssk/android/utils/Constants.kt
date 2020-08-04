package com.jssk.android.utils

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    val DD_MM_YYYY = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
    val EEEE = SimpleDateFormat("EEEE", Locale.ROOT)
    val HH_MM_A = SimpleDateFormat("hh:mm a", Locale.ROOT)
    val DD_MMM_YYYY_HH_MM_A = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ROOT)

    const val FUNCTIONS_REGION = "asia-east2"

    const val TERMS_URL = "https://www.google.com/"
    const val PRIVACY_URL = "https://www.google.com/"

    const val COLLECTION_USERS = "users"

    const val keyName = "userName"
    const val keyRole = "role"
    const val keyEmail = "email"
    const val keyMobile = "mobileNumber"

    const val ROLE_USER = "USER"

    const val PAGE_SIZE = 20L

    const val FOLDER_PROFILE_PICS = "profile_pictures"
}