package com.srm.mylauncherdemo.interfaces

import com.srm.launchersdk.model.AppDetails

interface OnItemClickListener {
    fun onItemClicked(appDetails: AppDetails)
}