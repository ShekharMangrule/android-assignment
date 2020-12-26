package com.srm.launchersdk

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import androidx.core.content.pm.PackageInfoCompat
import com.srm.launchersdk.model.AppDetails

object MyLauncherManager {

    private val appInfoList: MutableList<AppDetails> = ArrayList()

    fun getInstalledApps(context: Context): MutableList<AppDetails> {
        val pkgInfoList = context.packageManager.getInstalledPackages(0)
        //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);

        for (i in pkgInfoList.indices) {
            val pkgInfo = pkgInfoList[i]

            if (!isSystemPackage(pkgInfo)) {
                val appName = pkgInfo.applicationInfo.loadLabel(context.packageManager).toString()
                val icon = pkgInfo.applicationInfo.loadIcon(context.packageManager)
                val pkgName = pkgInfo.applicationInfo.packageName
                val versionCode = PackageInfoCompat.getLongVersionCode(pkgInfo)
                val versionName = pkgInfo.versionName
                val intent = context.packageManager.getLaunchIntentForPackage(pkgName)
                val activityName = pkgInfo.applicationInfo.name ?: "NA"

                appInfoList.add(
                    AppDetails(
                        appName = appName,
                        packageName = pkgName,
                        appIcon = icon,
                        mainActivityName = activityName,
                        versionCode = versionCode,
                        versionName = versionName,
                        launchIntent = intent
                    )
                )
            }
        }
        return appInfoList
    }

    fun sortListByAscending() {
        if (appInfoList.isNotEmpty()) {
            appInfoList.sortBy { it.appName }
        }
    }

    fun sortListByDescending() {
        if (appInfoList.isNotEmpty()) {
            appInfoList.sortByDescending { it.appName }
        }
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
}