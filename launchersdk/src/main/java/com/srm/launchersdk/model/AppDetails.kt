package com.srm.launchersdk.model

import android.content.Intent
import android.graphics.drawable.Drawable

data class AppDetails(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable,
    val mainActivityName: String,
    val versionCode: Long,
    val versionName: String,
    val launchIntent: Intent?
)